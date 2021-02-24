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
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.databinding.ActivityMainBinding
import fobo66.exchangecourcesbelarus.list.BestCurrencyRatesAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import reactivecircus.flowbinding.android.widget.checkedChanges
import reactivecircus.flowbinding.appcompat.itemClicks
import reactivecircus.flowbinding.swiperefreshlayout.refreshes
import kotlin.LazyThreadSafetyMode.NONE

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  private val viewModel: MainViewModel by viewModels()
  private lateinit var binding: ActivityMainBinding

  private lateinit var bestCoursesAdapter: BestCurrencyRatesAdapter

  private val errorSnackbar: Snackbar by lazy(mode = NONE) {
    Snackbar.make(binding.root, R.string.get_data_error, Snackbar.LENGTH_SHORT)
  }

  private var aboutDialog: AlertDialog? = null

  private val showRefreshSpinnerRunnable = { binding.swipeRefresh.isRefreshing = true }
  private val hideRefreshSpinnerRunnable = { binding.swipeRefresh.isRefreshing = false }

  @ExperimentalCoroutinesApi
  private val requestPermission =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
      if (granted) {
        refreshExchangeRates()
      } else {
        hideRefreshSpinner()
      }
    }

  @ExperimentalCoroutinesApi
  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.ExchangeCoursesTheme)
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupLayout()
    setupCoursesList()
    setupSwipeRefreshLayout()
    setupBuyOrSellObserver()
  }

  private fun processMenuItemClick(item: MenuItem): Boolean =
    when (item.itemId) {
      R.id.action_settings -> {
        SettingsActivity.start(this)
        true
      }
      R.id.action_about -> {
        if (aboutDialog == null) {
          aboutDialog = AlertDialog.Builder(this)
            .setTitle(R.string.title_about)
            .setMessage(R.string.about_app_description)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
              dialog.dismiss()
            }
            .create()
        }

        if (aboutDialog?.isShowing == false) {
          aboutDialog?.show()
          aboutDialog?.findViewById<TextView>(android.R.id.message)?.movementMethod =
            LinkMovementMethod.getInstance()
        }
        true
      }
      else -> false
    }

  @ExperimentalCoroutinesApi
  private fun refreshExchangeRates() {
    if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
      != PackageManager.PERMISSION_GRANTED
    ) {
      requestPermission.launch(permission.ACCESS_COARSE_LOCATION)
    } else {
      showRefreshSpinner()

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

  @ExperimentalCoroutinesApi
  private fun setupBuyOrSellObserver() {
    lifecycleScope.launchWhenCreated {
      viewModel.buyOrSell.collect {
        refreshExchangeRates()
        setBuySellIndicator(it)
      }
    }
  }

  private fun prepareMenu(menu: Menu) {
    val item = menu.findItem(R.id.action_buysell)
    val control: SwitchCompat = item.actionView as SwitchCompat
    control.isChecked = viewModel.buyOrSell.value == true

    setBuySellIndicator(control.isChecked)

    control.checkedChanges()
      .onEach {
        showRefreshSpinner()

        viewModel.updateBuySell(it)
      }
      .launchIn(lifecycleScope)
  }

  private fun showRefreshSpinner() {
    binding.swipeRefresh.post(showRefreshSpinnerRunnable)
  }

  private fun hideRefreshSpinner() {
    binding.swipeRefresh.post(hideRefreshSpinnerRunnable)
  }

  private fun setBuySellIndicator(isBuy: Boolean) {
    binding.buysellIndicator.setText(
      if (isBuy) {
        R.string.buy
      } else {
        R.string.sell
      }
    )
  }

  @ExperimentalCoroutinesApi
  private fun setupSwipeRefreshLayout() {
    binding.swipeRefresh.refreshes()
      .onEach {
        refreshExchangeRates()
      }
      .launchIn(lifecycleScope)

    binding.swipeRefresh.setColorSchemeResources(R.color.primary_color)
  }

  override fun onStop() {
    super.onStop()

    aboutDialog = null

  }

  @ExperimentalCoroutinesApi
  private fun setupCoursesList() {
    bestCoursesAdapter = BestCurrencyRatesAdapter()
    binding.coursesList.apply {
      layoutManager = LinearLayoutManager(context)
      itemAnimator = DefaultItemAnimator()
      adapter = bestCoursesAdapter
      setHasFixedSize(true)
    }

    lifecycleScope.launchWhenCreated {
      viewModel.bestCurrencyRates
        .catch { processError() }
        .collectLatest {
          bestCoursesAdapter.submitList(it)
          hideRefreshSpinner()
          binding.swipeRefresh.isEnabled = false
        }

      viewModel.errors.collectLatest {
        processError()
      }
    }
  }

  private fun processError() {
    hideRefreshSpinner()
    binding.swipeRefresh.isEnabled = true
    errorSnackbar.show()
  }

  private fun setupLayout() {
    WindowCompat.setDecorFitsSystemWindows(window, false)

    prepareMenu(binding.toolbar.menu)

    binding.toolbar.itemClicks()
      .onEach { processMenuItemClick(it) }
      .launchIn(lifecycleScope)

    Insetter.builder().applySystemWindowInsetsToMargin(Side.TOP or Side.RIGHT or Side.LEFT)
      .applyToView(binding.toolbar)
    Insetter.builder().applySystemWindowInsetsToPadding(Side.RIGHT or Side.LEFT)
      .applyToView(binding.coursesList)
  }
}