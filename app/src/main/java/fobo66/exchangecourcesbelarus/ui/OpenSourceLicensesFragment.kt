package fobo66.exchangecourcesbelarus.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.chrisbanes.insetter.applyInsetter
import fobo66.exchangecourcesbelarus.databinding.FragmentOpenSourceLicensesBinding

class OpenSourceLicensesFragment : Fragment() {
  private var _binding: FragmentOpenSourceLicensesBinding? = null
  private val binding: FragmentOpenSourceLicensesBinding
    get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentOpenSourceLicensesBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding.licensesView.applyInsetter {
      type(statusBars = true, navigationBars = true, systemGestures = true) {
        margin(left = true, right = true)
      }
    }
    binding.licensesView.loadUrl("file:///android_asset/open_source_licenses.html")
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
