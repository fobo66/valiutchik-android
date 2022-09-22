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
import androidx.core.app.ActivityOptionsCompat
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.swiperefreshlayout.refreshes

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
        viewModel.hideProgress()
      }
    }

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
      viewModel.refreshExchangeRates()
    }
  }

  private fun setupSwipeRefreshLayout() {
    binding.swipeRefresh.refreshes()
      .onEach {
        refreshExchangeRates()
      }
      .launchIn(viewLifecycleOwner.lifecycleScope)

    viewModel.progress
      .onEach {
        binding.swipeRefresh.isRefreshing = it
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
    val mapIntent = viewModel.findBankOnMap(bankName)

    if (mapIntent != null) {
      ActivityCompat.startActivity(
        requireContext(),
        Intent.createChooser(mapIntent, getString(string.open_map)),
        ActivityOptionsCompat.makeBasic().toBundle()
      )
    } else {
      Snackbar.make(binding.root, string.maps_app_required, Snackbar.LENGTH_LONG).show()
    }
  }

  private fun processError() {
    Snackbar.make(binding.root, string.get_data_error, Snackbar.LENGTH_SHORT).show()
  }
}