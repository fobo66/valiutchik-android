package fobo66.exchangecourcesbelarus.ui

import android.Manifest.permission
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import android.widget.CompoundButton
import android.widget.RelativeLayout
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.databinding.ActivityMainBinding
import fobo66.exchangecourcesbelarus.di.injector
import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.list.BestCoursesAdapter
import fobo66.exchangecourcesbelarus.model.CurrencyRateService
import fobo66.exchangecourcesbelarus.util.Constants
import fobo66.exchangecourcesbelarus.util.Constants.EXTRA_BUYORSELL
import fobo66.exchangecourcesbelarus.util.ExceptionHandler

class MainActivity : BaseActivity() {
  private lateinit var viewModel: MainViewModel
  private lateinit var binding: ActivityMainBinding

  var buyOrSell = false

  private lateinit var bestCoursesAdapter: BestCoursesAdapter
  private val previousBest: MutableList<BestCourse> = mutableListOf()
  private lateinit var bestCourseRef: DatabaseReference
  private lateinit var receiver: BroadcastReceiver

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    viewModel =
      ViewModelProvider(this, injector.mainViewModelFactory()).get(MainViewModel::class.java)

    updateValuesFromBundle(savedInstanceState)
    constructBroadcastReceiver()
    setupFirebaseReference()
    setupLayout()
    setupPlayServices()
    setupCoursesList()
    setupSwipeRefreshLayout()
    setBuySellIndicator()
  }

  override fun onStart() {
    super.onStart()
    buyOrSell = prefs.getBoolean(EXTRA_BUYORSELL, false)
    googleApiClient.connect()
    val intentFilter = IntentFilter(Constants.BROADCAST_ACTION_SUCCESS)
    intentFilter.addAction(Constants.BROADCAST_ACTION_ERROR)
    LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter)
  }

  override fun onStop() {
    super.onStop()
    prefs.edit {
      putBoolean(EXTRA_BUYORSELL, buyOrSell)
    }
    if (googleApiClient.isConnected) {
      googleApiClient.disconnect()
    }
    LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    bestCourseRef.onDisconnect()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    val item = menu.findItem(R.id.action_buysell)
    item.setActionView(R.layout.switch_actionbar)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val id = item.itemId
    return when (id) {
      R.id.action_settings -> {
        val settingsIntent = Intent(this, SettingsActivity::class.java)
        startActivity(settingsIntent)
        true
      }
      R.id.action_update -> {
        binding.swipeRefresh.isRefreshing = true
        try {
          fetchCourses(true)
        } catch (e: Exception) {
          ExceptionHandler.handleException(e)
          resolveUserCity()
        }
        true
      }
      R.id.action_about -> {
        startActivity(Intent(this, AboutActivity::class.java))
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onPrepareOptionsMenu(menu: Menu): Boolean {
    super.onPrepareOptionsMenu(menu)
    val item = menu.findItem(R.id.action_buysell)
    val rootView = item.actionView as RelativeLayout
    val control: SwitchCompat = rootView.findViewById(R.id.switchForActionBar)
    control.isChecked = buyOrSell
    setBuySellIndicator()
    control.setOnCheckedChangeListener { compoundButton: CompoundButton, _ ->
      binding.swipeRefresh.isRefreshing = true
      buyOrSell = compoundButton.isChecked
      bestCoursesAdapter.setBuyOrSell(buyOrSell)
      if (googleApiClient.isConnected && userCity == null) {
        resolveUserCity()
      }

      fetchCourses(false)

      setBuySellIndicator()
    }
    return true
  }

  public override fun onSaveInstanceState(savedInstanceState: Bundle) {
    super.onSaveInstanceState(savedInstanceState)
    savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, addressRequested)
    savedInstanceState.putString(LOCATION_ADDRESS_KEY, userCity)
    savedInstanceState.putBoolean(EXTRA_BUYORSELL, buyOrSell)
  }

  override fun onRequestPermissionsResult(
    requestCode: Int, permissions: Array<String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == Constants.LOCATION_PERMISSION_REQUEST) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        resolveUserCity()
      } else {
        hideRefreshSpinner()
      }
    } else if (requestCode == Constants.INTERNET_PERMISSIONS_REQUEST) {
      if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        try {
          fetchCourses(true)
        } catch (e: Exception) {
          ExceptionHandler.handleException(e)
          Snackbar.make(binding.swipeRefresh, R.string.get_data_error, Snackbar.LENGTH_SHORT).show()
        }
      } else {
        hideRefreshSpinner()
      }
    }
  }

  override fun fetchCourses(forceReload: Boolean) {
    if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_NETWORK_STATE)
      != PackageManager.PERMISSION_GRANTED
      && ActivityCompat.checkSelfPermission(this, permission.INTERNET)
      != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(
        this,
        arrayOf(permission.ACCESS_NETWORK_STATE, permission.INTERNET),
        Constants.INTERNET_PERMISSIONS_REQUEST
      )
    } else {
      CurrencyRateService.fetchCourses(this, userCity, buyOrSell)
      hideRefreshSpinner()
    }
  }

  private fun hideRefreshSpinner() {
    binding.swipeRefresh.post { binding.swipeRefresh.isRefreshing = false }
  }

  private fun setBuySellIndicator() {
    if (buyOrSell) {
      binding.buysellIndicator.setText(R.string.sell)
    } else {
      binding.buysellIndicator.setText(R.string.buy)
    }
  }

  private fun setupSwipeRefreshLayout() {
    binding.swipeRefresh.setOnRefreshListener {
      if (userCity == null) {
        resolveUserCity()
      } else {
        try {
          fetchCourses(false)
        } catch (e: Exception) {
          ExceptionHandler.handleException(e)
          Snackbar.make(binding.root, R.string.get_data_error, Snackbar.LENGTH_SHORT).show()
        }
      }
    }
    binding.swipeRefresh.setColorSchemeResources(R.color.primary_color)
    binding.swipeRefresh.post { binding.swipeRefresh.isRefreshing = true }
  }

  private fun setupPlayServices() {
    try {
      ProviderInstaller.installIfNeeded(this)
    } catch (e: GooglePlayServicesRepairableException) {
      GoogleApiAvailability.getInstance().showErrorNotification(this, e.connectionStatusCode)
    } catch (e: GooglePlayServicesNotAvailableException) {
      GoogleApiAvailability.getInstance().showErrorDialogFragment(this, e.errorCode, 0)
    }
  }

  private fun setupCoursesList() {
    bestCoursesAdapter = BestCoursesAdapter(previousBest)
    binding.coursesList.apply {
      layoutManager = LinearLayoutManager(context)
      itemAnimator = DefaultItemAnimator()
      adapter = bestCoursesAdapter
      setHasFixedSize(true)
    }
  }

  private fun setupLayout() {
    binding.root.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)

    setupLightNavigationBar()

    setSupportActionBar(binding.toolbar)
  }

  private fun setupLightNavigationBar() {
    if (VERSION.SDK_INT >= VERSION_CODES.O) {
      val mode = resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)

      if (mode != Configuration.UI_MODE_NIGHT_YES) {
        window.decorView.systemUiVisibility = FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS or
            SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
      }
    }
  }

  private fun setupFirebaseReference() {
    bestCourseRef = bestCoursesReference
    bestCourseRef.keepSynced(false)
    bestCourseRef.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        val t: GenericTypeIndicator<List<BestCourse>> =
          object : GenericTypeIndicator<List<BestCourse>>() {}
        previousBest.clear()
        previousBest.addAll(dataSnapshot.getValue(t)!!)
      }

      override fun onCancelled(databaseError: DatabaseError) {
        Log.e(
          TAG,
          "onCancelled: Firebase failed: " + databaseError.details
        )
      }
    })
  }

  private val bestCoursesReference: DatabaseReference
    get() = if (buyOrSell) {
      FirebaseDatabase.getInstance().getReference("bestcourse_buy")
    } else {
      FirebaseDatabase.getInstance().getReference("bestcourse_sell")
    }

  private fun onDataError() {
    runOnUiThread {
      Snackbar.make(binding.root, R.string.courses_unavailable_info, Snackbar.LENGTH_LONG)
        .show()
      bestCoursesAdapter.onDataUpdate(previousBest)
      binding.swipeRefresh.isRefreshing = false
    }
  }

  private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
    if (savedInstanceState != null) {
      if (savedInstanceState.containsKey(ADDRESS_REQUESTED_KEY)) {
        addressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY)
      }
      if (savedInstanceState.containsKey(LOCATION_ADDRESS_KEY)) {
        userCity = savedInstanceState.getString(LOCATION_ADDRESS_KEY)
      }
      if (savedInstanceState.containsKey(EXTRA_BUYORSELL)) {
        buyOrSell = savedInstanceState.getBoolean(EXTRA_BUYORSELL)
      }
    }
  }

  private fun constructBroadcastReceiver() {
    receiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        val intentAction = intent.action
        if (intentAction != null) {
          if (intentAction == Constants.BROADCAST_ACTION_SUCCESS) {
            val extra: List<BestCourse> =
              intent.getParcelableArrayListExtra(Constants.EXTRA_BESTCOURSES) ?: emptyList()
            bestCoursesAdapter.onDataUpdate(extra)
            bestCoursesReference.setValue(extra)
          } else if (intentAction == Constants.BROADCAST_ACTION_ERROR) {
            onDataError()
          }
        } else {
          onDataError()
        }
      }
    }
  }

  companion object {
    private const val TAG = "MainActivity"
  }
}