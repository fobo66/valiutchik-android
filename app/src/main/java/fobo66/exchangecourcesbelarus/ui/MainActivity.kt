package fobo66.exchangecourcesbelarus.ui

import android.Manifest.permission
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.databinding.ActivityMainBinding
import fobo66.exchangecourcesbelarus.di.injector
import fobo66.exchangecourcesbelarus.list.BestCurrencyRatesAdapter
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.LazyThreadSafetyMode.NONE

class MainActivity : AppCompatActivity(), OnMenuItemClickListener {
  private lateinit var viewModel: MainViewModel
  private lateinit var binding: ActivityMainBinding

  private lateinit var bestCoursesAdapter: BestCurrencyRatesAdapter

  private val errorSnackbar: Snackbar by lazy(mode = NONE) {
    Snackbar.make(binding.root, R.string.get_data_error, Snackbar.LENGTH_SHORT)
  }

  private val aboutDialog: AlertDialog by lazy(mode = NONE) {
    AlertDialog.Builder(this)
      .setTitle(R.string.title_about)
      .setMessage(R.string.about_app_description)
      .setPositiveButton(android.R.string.ok) { dialog, _ ->
        dialog.dismiss()
      }
      .create()
  }

  private val showRefreshSpinnerRunnable = { binding.swipeRefresh.isRefreshing = true }
  private val hideRefreshSpinnerRunnable = { binding.swipeRefresh.isRefreshing = false }

  private val requestPermission =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
      if (granted) {
        refreshExchangeRates()
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

  override fun onMenuItemClick(item: MenuItem?): Boolean =
    when (item?.itemId) {
      R.id.action_settings -> {
        SettingsActivity.start(this)
        true
      }
      R.id.action_about -> {
        if (!aboutDialog.isShowing) {
          aboutDialog.show()
          aboutDialog.findViewById<TextView>(android.R.id.message)?.movementMethod =
            LinkMovementMethod.getInstance()
        }
        true
      }
      else -> false
    }

  private fun refreshExchangeRates() {
    if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
      != PackageManager.PERMISSION_GRANTED
    ) {
      requestPermission.launch(permission.ACCESS_COARSE_LOCATION)
    } else {
      showRefreshSpinner()
      Firebase.analytics.logEvent("load_exchange_rates", Bundle.EMPTY)
      val locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
      lifecycleScope.launch {
        val location: Location? = locationProviderClient.lastLocation.await()
        if (location != null) {
          viewModel.refreshExchangeRates(location.latitude, location.longitude)
        } else {
          viewModel.refreshExchangeRates(0.0, 0.0)
        }
      }
    }
  }

  private fun setupBuyOrSellObserver() {
    viewModel.buyOrSell.observe(this) {
      refreshExchangeRates()
      setBuySellIndicator(it)
    }
  }

  private fun prepareMenu(menu: Menu) {
    val item = menu.findItem(R.id.action_buysell)
    val control: SwitchCompat = item.actionView as SwitchCompat
    control.isChecked = viewModel.buyOrSell.value == true

    setBuySellIndicator(control.isChecked)

    control.setOnCheckedChangeListener { _, isChecked ->
      showRefreshSpinner()

      Firebase.analytics.logEvent("buy_sell_switch_toggled") {
        param(FirebaseAnalytics.Param.VALUE, isChecked.toString())
      }
      viewModel.updateBuySell(isChecked)
    }
  }

  private fun showRefreshSpinner() {
    binding.swipeRefresh.post(showRefreshSpinnerRunnable)
  }

  private fun hideRefreshSpinner() {
    binding.swipeRefresh.post(hideRefreshSpinnerRunnable)
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
      refreshExchangeRates()
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
      binding.swipeRefresh.isEnabled = false
    }
    viewModel.errors.observe(this) {
      hideRefreshSpinner()
      binding.swipeRefresh.isEnabled = true
      errorSnackbar.show()
    }
  }

  private fun setupLayout() {
    WindowCompat.setDecorFitsSystemWindows(window, false)

    prepareMenu(binding.toolbar.menu)

    binding.toolbar.setOnMenuItemClickListener(this)

    Insetter.builder().applySystemWindowInsetsToMargin(Side.TOP).applyToView(binding.toolbar)
  }
}