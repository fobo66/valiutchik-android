package fobo66.exchangecourcesbelarus.ui

import android.Manifest.permission
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import reactivecircus.flowbinding.swiperefreshlayout.refreshes
import timber.log.Timber

/**
 * Main fragment with the list of currency rates
 */
@AndroidEntryPoint
class MainFragment : Fragment() {

  private var _binding: FragmentMainBinding? = null

  private var bestCoursesAdapter: BestCurrencyRatesAdapter? = null

  private val viewModel: MainViewModel by viewModels()

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
    Insetter.builder().padding(
      windowInsetTypesOf(ime = true, statusBars = true, navigationBars = true),
      Side.RIGHT or Side.LEFT
    )
      .applyToView(binding.coursesList)

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
          val mapUri =
            Uri.parse(viewModel.resolveMapQuery(bankName.toString()))
          val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)

          if (mapIntent.resolveActivity(requireContext().packageManager) != null) {
            ActivityCompat.startActivity(requireContext(), mapIntent, null)
          } else {
            Timber.e("Failed to show banks on map: maps app not found")
            Snackbar.make(binding.root, R.string.maps_app_required, Snackbar.LENGTH_LONG).show()
          }
        }
    }

    viewLifecycleOwner.lifecycleScope.launchWhenResumed {
      bestCoursesAdapter?.currencyRateLongClicked
        ?.collect { (currencyName, currencyValue) ->
          val clipData = ClipData.newPlainText(currencyName, currencyValue)
          val clipboardManager = requireContext().getSystemService<ClipboardManager>()
          clipboardManager?.setPrimaryClip(clipData)
          Snackbar.make(binding.root, R.string.currency_value_copied, Snackbar.LENGTH_SHORT).show()
        }
    }
  }

  private fun processError() {
    hideRefreshSpinner()
    binding.swipeRefresh.isEnabled = true
    Snackbar.make(binding.root, R.string.get_data_error, Snackbar.LENGTH_SHORT).show()
  }
}
