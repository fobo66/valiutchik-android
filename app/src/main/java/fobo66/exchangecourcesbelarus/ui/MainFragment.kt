package fobo66.exchangecourcesbelarus.ui

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.databinding.FragmentMainBinding
import fobo66.exchangecourcesbelarus.list.BestCurrencyRatesAdapter
import fobo66.exchangecourcesbelarus.util.LocationResolver
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import reactivecircus.flowbinding.swiperefreshlayout.refreshes
import timber.log.Timber
import javax.inject.Inject

/**
 * Main fragment with the list of currency rates
 */
@AndroidEntryPoint
class MainFragment : Fragment() {

  private var _binding: FragmentMainBinding? = null

  private var bestCoursesAdapter: BestCurrencyRatesAdapter? = null

  private val viewModel: MainViewModel by viewModels()

  @Inject
  lateinit var locationResolver: LocationResolver

  private val binding: FragmentMainBinding
    get() = _binding!!

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

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding.coursesList.applyInsetter {
      type(statusBars = true, navigationBars = true, systemGestures = true) {
        padding(left = true, right = true)
      }
    }

    setupCoursesList()
    setupSwipeRefreshLayout()
    refreshExchangeRates()
  }

  override fun onDestroyView() {
    super.onDestroyView()

    _binding = null
    bestCoursesAdapter = null
  }

  private fun refreshExchangeRates() {
    if (ActivityCompat.checkSelfPermission(requireContext(), permission.ACCESS_COARSE_LOCATION)
      != PackageManager.PERMISSION_GRANTED
    ) {
      requestPermission.launch(permission.ACCESS_COARSE_LOCATION)
    } else {
      showRefreshSpinner()

      viewLifecycleOwner.lifecycleScope.launch {
        val (latitude, longitude) = locationResolver.resolveLocation(requireActivity())
        viewModel.refreshExchangeRates(latitude, longitude)
      }
    }
  }

  private fun showRefreshSpinner() {
    binding.swipeRefresh.post(showRefreshSpinnerRunnable)
  }

  private fun hideRefreshSpinner() {
    binding.swipeRefresh.post(hideRefreshSpinnerRunnable)
  }

  private fun setupSwipeRefreshLayout() {
    binding.swipeRefresh.refreshes()
      .onEach {
        refreshExchangeRates()
      }
      .launchIn(viewLifecycleOwner.lifecycleScope)

    binding.swipeRefresh.setColorSchemeResources(R.color.primary_color)
  }

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

    viewLifecycleOwner.lifecycleScope.launchWhenResumed {
      bestCoursesAdapter?.currencyRateClicked
        ?.collect { bankName ->
          searchBankOnMap(bankName)
        }
    }

    viewLifecycleOwner.lifecycleScope.launchWhenResumed {
      bestCoursesAdapter?.currencyRateLongClicked
        ?.collect { (currencyName, currencyValue) ->
          viewModel.copyCurrencyRateToClipboard(currencyName, currencyValue)
          Snackbar.make(binding.root, string.currency_value_copied, Snackbar.LENGTH_SHORT).show()
        }
    }
  }

  private fun searchBankOnMap(bankName: CharSequence) {
    val mapUri =
      viewModel.prepareMapUri(bankName.toString())
    val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)

    if (mapIntent.resolveActivity(requireContext().packageManager) != null) {
      ActivityCompat.startActivity(requireContext(), mapIntent, null)
    } else {
      Timber.e("Failed to show banks on map: maps app not found")
      Snackbar.make(binding.root, string.maps_app_required, Snackbar.LENGTH_LONG).show()
    }
  }

  private fun processError() {
    hideRefreshSpinner()
    binding.swipeRefresh.isEnabled = true
    Snackbar.make(binding.root, string.get_data_error, Snackbar.LENGTH_SHORT).show()
  }
}
