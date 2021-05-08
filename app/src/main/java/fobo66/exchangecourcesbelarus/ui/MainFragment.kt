package fobo66.exchangecourcesbelarus.ui

import android.Manifest.permission
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side
import dev.chrisbanes.insetter.windowInsetTypesOf
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.databinding.FragmentMainBinding
import fobo66.exchangecourcesbelarus.list.BestCurrencyRatesAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import reactivecircus.flowbinding.swiperefreshlayout.refreshes

/**
 * Main fragment with the list of currency rates
 */
@AndroidEntryPoint
class MainFragment : Fragment() {

  private var _binding: FragmentMainBinding? = null

  private var bestCoursesAdapter: BestCurrencyRatesAdapter? = null

  private val viewModel: MainViewModel by activityViewModels()

  private val binding: FragmentMainBinding
    get() = _binding!!

  @ExperimentalCoroutinesApi
  private val requestPermission =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
      if (granted) {
        refreshExchangeRates()
      } else {
        hideRefreshSpinner()
      }
    }

  private val showRefreshSpinnerRunnable = { binding.swipeRefresh.isRefreshing = true }
  private val hideRefreshSpinnerRunnable = { binding.swipeRefresh.isRefreshing = false }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMainBinding.inflate(inflater, container, false)
    return binding.root
  }

  @ExperimentalCoroutinesApi
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    Insetter.builder().padding(
      windowInsetTypesOf(ime = true, statusBars = true, navigationBars = true),
      Side.RIGHT or Side.LEFT
    )
      .applyToView(binding.coursesList)

    setupCoursesList()
    setupSwipeRefreshLayout()
    setupBuyOrSellObserver()
  }

  override fun onDestroyView() {
    super.onDestroyView()

    _binding = null
    bestCoursesAdapter = null
  }

  @ExperimentalCoroutinesApi
  private fun refreshExchangeRates() {
    if (ActivityCompat.checkSelfPermission(requireContext(), permission.ACCESS_COARSE_LOCATION)
      != PackageManager.PERMISSION_GRANTED
    ) {
      requestPermission.launch(permission.ACCESS_COARSE_LOCATION)
    } else {
      showRefreshSpinner()

      val locationProviderClient =
        LocationServices.getFusedLocationProviderClient(requireActivity())
      viewLifecycleOwner.lifecycleScope.launch {
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
    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
      viewModel.buyOrSell.collectLatest {
        refreshExchangeRates()
        setBuySellIndicator(it)
      }
    }
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
      .launchIn(viewLifecycleOwner.lifecycleScope)

    binding.swipeRefresh.setColorSchemeResources(R.color.primary_color)
  }

  @ExperimentalCoroutinesApi
  private fun setupCoursesList() {
    bestCoursesAdapter = BestCurrencyRatesAdapter()
    binding.coursesList.apply {
      itemAnimator = DefaultItemAnimator()
      adapter = bestCoursesAdapter
      setHasFixedSize(true)
    }

    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
      viewModel.bestCurrencyRates
        .catch { processError() }
        .collectLatest {
          bestCoursesAdapter?.submitList(it)
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
    Snackbar.make(binding.root, R.string.get_data_error, Snackbar.LENGTH_SHORT).show()
  }
}
