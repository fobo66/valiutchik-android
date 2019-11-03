package fobo66.exchangecourcesbelarus.ui

import android.Manifest.permission
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.list.BestCoursesAdapter
import fobo66.exchangecourcesbelarus.model.CurrencyRateService
import fobo66.exchangecourcesbelarus.util.Constants
import fobo66.exchangecourcesbelarus.util.Constants.EXTRA_BUYORSELL
import fobo66.exchangecourcesbelarus.util.ExceptionHandler

class MainActivity : BaseActivity() {
  private lateinit var viewModel: MainViewModel
  private lateinit var swipeRefreshLayout: SwipeRefreshLayout
  private lateinit var coursesList: RecyclerView
  private lateinit var buysellIndicator: TextView
  var buyOrSell = false
  private lateinit var bestCoursesAdapter: BestCoursesAdapter
  private val previousBest: MutableList<BestCourse> = ArrayList()
  private lateinit var bestCourseRef: DatabaseReference
  private var firebaseRegistering = true
  private lateinit var receiver: BroadcastReceiver

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    prefs = PreferenceManager.getDefaultSharedPreferences(this)
    viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
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
    val editor = prefs.edit()
    editor.putBoolean(EXTRA_BUYORSELL, buyOrSell)
    editor.apply()
    if (googleApiClient.isConnected) {
      googleApiClient.disconnect()
    }
    LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    bestCourseRef.onDisconnect()
    firebaseRegistering = false
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
        swipeRefreshLayout.isRefreshing = true
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
    if (control.isChecked) {
      buysellIndicator.setText(R.string.sell)
    } else {
      buysellIndicator.setText(R.string.buy)
    }
    control.setOnCheckedChangeListener { compoundButton: CompoundButton, _ ->
      swipeRefreshLayout.isRefreshing = true
      buyOrSell = compoundButton.isChecked
      bestCoursesAdapter.setBuyOrSell(buyOrSell)
      if (googleApiClient.isConnected && userCity == null) {
        resolveUserCity()
      }
      try {
        fetchCourses(false)
      } catch (e: Exception) {
        ExceptionHandler.handleException(e)
        Snackbar.make(swipeRefreshLayout, R.string.get_data_error, Snackbar.LENGTH_SHORT).show()
      }
      setBuySellIndicator()
    }
    return true
  }

  public override fun onSaveInstanceState(savedInstanceState: Bundle) {
    savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, addressRequested)
    savedInstanceState.putString(LOCATION_ADDRESS_KEY, userCity)
    savedInstanceState.putBoolean(EXTRA_BUYORSELL, buyOrSell)
    savedInstanceState.putBoolean("firebase_registering", firebaseRegistering)
    super.onSaveInstanceState(savedInstanceState)
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
          Snackbar.make(swipeRefreshLayout, R.string.get_data_error, Snackbar.LENGTH_SHORT).show()
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
    swipeRefreshLayout.post { swipeRefreshLayout.isRefreshing = false }
  }

  private fun setBuySellIndicator() {
    if (buyOrSell) {
      buysellIndicator.setText(R.string.sell)
    } else {
      buysellIndicator.setText(R.string.buy)
    }
  }

  private fun setupSwipeRefreshLayout() {
    swipeRefreshLayout.setOnRefreshListener {
      if (userCity == null) {
        resolveUserCity()
      } else {
        try {
          fetchCourses(false)
        } catch (e: Exception) {
          ExceptionHandler.handleException(e)
          Snackbar.make(swipeRefreshLayout, R.string.get_data_error, Snackbar.LENGTH_SHORT).show()
        }
      }
    }
    swipeRefreshLayout.setColorSchemeResources(R.color.primary_color)
    swipeRefreshLayout.post { swipeRefreshLayout.isRefreshing = true }
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
    coursesList.apply {
      layoutManager = LinearLayoutManager(context)
      itemAnimator = DefaultItemAnimator()
      adapter = bestCoursesAdapter
      setHasFixedSize(true)
    }
  }

  private fun setupLayout() {
    swipeRefreshLayout = findViewById(R.id.swipe_refresh)
    coursesList = findViewById(R.id.rv)
    buysellIndicator = findViewById(R.id.buysell_indicator)
    val toolbar = findViewById<Toolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)
    toolbar.doOnApplyWindowInsets { view, insets, initialState ->
      view.updatePadding(
        top = initialState.paddings.top + insets.systemWindowInsetTop
      )
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
    Snackbar.make(swipeRefreshLayout, R.string.courses_unavailable_info, Snackbar.LENGTH_LONG)
      .show()
    runOnUiThread {
      bestCoursesAdapter.onDataUpdate(previousBest)
      swipeRefreshLayout.isRefreshing = false
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
      if (savedInstanceState.containsKey(Constants.FIREBASE_REGISTERING_KEY)) {
        firebaseRegistering = savedInstanceState.getBoolean(Constants.FIREBASE_REGISTERING_KEY)
      }
    }
  }

  private fun constructBroadcastReceiver() {
    receiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        val intentAction = intent.action
        if (intentAction != null) {
          if (intentAction == Constants.BROADCAST_ACTION_SUCCESS) {
            val extra: ArrayList<BestCourse> =
              intent.getParcelableArrayListExtra(Constants.EXTRA_BESTCOURSES)
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