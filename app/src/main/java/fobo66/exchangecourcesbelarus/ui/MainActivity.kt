package fobo66.exchangecourcesbelarus.ui

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
import android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
import android.widget.CompoundButton
import android.widget.RelativeLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.google.firebase.analytics.FirebaseAnalytics
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.databinding.ActivityMainBinding
import fobo66.exchangecourcesbelarus.di.injector
import fobo66.exchangecourcesbelarus.list.BestCurrencyRatesAdapter
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
  private lateinit var viewModel: MainViewModel
  private lateinit var binding: ActivityMainBinding

  private lateinit var bestCoursesAdapter: BestCurrencyRatesAdapter

  private val requestPermission =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
      if (granted) {
        fetchCourses()
      } else {
        hideRefreshSpinner()
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    viewModel =
      ViewModelProvider(this, injector.mainViewModelFactory()).get(MainViewModel::class.java)

    setupLayout()
    setupCoursesList()
    setupSwipeRefreshLayout()
    setupBuyOrSellObserver()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    val item = menu.findItem(R.id.action_buysell)
    item.setActionView(R.layout.switch_actionbar)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean =
    when (item.itemId) {
      R.id.action_settings -> {
        val settingsIntent = Intent(this, SettingsActivity::class.java)
        startActivity(settingsIntent)
        true
      }
      R.id.action_update -> {
        fetchCourses()
        true
      }
      R.id.action_about -> {
        startActivity(Intent(this, AboutActivity::class.java))
        true
      }
      else -> super.onOptionsItemSelected(item)
    }

  override fun onPrepareOptionsMenu(menu: Menu): Boolean {
    super.onPrepareOptionsMenu(menu)
    val item = menu.findItem(R.id.action_buysell)
    val rootView = item.actionView as RelativeLayout
    val control: SwitchCompat = rootView.findViewById(R.id.switchForActionBar)
    control.isChecked = viewModel.buyOrSell.value == true
    setBuySellIndicator(control.isChecked)
    control.setOnCheckedChangeListener { compoundButton: CompoundButton, _ ->
      showRefreshSpinner()
      val params = Bundle().apply {
        putBoolean(FirebaseAnalytics.Param.VALUE, compoundButton.isChecked)
      }
      FirebaseAnalytics.getInstance(this).logEvent("buy_sell_switch_toggled", params)
      viewModel.updateBuySell(compoundButton.isChecked)
    }
    return true
  }

  fun fetchCourses() {
    if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
      != PackageManager.PERMISSION_GRANTED) {
      requestPermission.launch(permission.ACCESS_COARSE_LOCATION)
    } else {
      showRefreshSpinner()
      FirebaseAnalytics.getInstance(this).logEvent("load_exchange_rates", Bundle.EMPTY)
      val locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
      lifecycleScope.launch {
        val location: Location? = locationProviderClient.lastLocation.await()
        if (location != null) {
          viewModel.loadExchangeRates(location.latitude, location.longitude)
        } else {
          viewModel.loadExchangeRates(0.0, 0.0)
        }
      }
    }
  }

  private fun setupBuyOrSellObserver() {
    viewModel.buyOrSell.observe(this) {
      fetchCourses()
      setBuySellIndicator(it)
    }
  }

  private fun showRefreshSpinner() {
    binding.swipeRefresh.post { binding.swipeRefresh.isRefreshing = true }
  }

  private fun hideRefreshSpinner() {
    binding.swipeRefresh.post { binding.swipeRefresh.isRefreshing = false }
  }

  private fun setBuySellIndicator(buyOrSell: Boolean) {
    binding.buysellIndicator.setText(
      if (buyOrSell) {
        R.string.buy
      } else {
        R.string.sell
      }
    )
  }

  private fun setupSwipeRefreshLayout() {
    binding.swipeRefresh.setOnRefreshListener {
      fetchCourses()
    }
    binding.swipeRefresh.setColorSchemeResources(R.color.primary_color)
  }

  private fun setupCoursesList() {
    bestCoursesAdapter = BestCurrencyRatesAdapter()
    binding.coursesList.apply {
      layoutManager = LinearLayoutManager(context)
      itemAnimator = DefaultItemAnimator()
      adapter = bestCoursesAdapter
      setHasFixedSize(true)
    }
    viewModel.bestCurrencyRates.observe(this) {
      bestCoursesAdapter.submitList(it)
      hideRefreshSpinner()
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

      if (Configuration.UI_MODE_NIGHT_YES != mode) {
        window.decorView.systemUiVisibility = FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS or
            SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
      }
    }
  }
}
