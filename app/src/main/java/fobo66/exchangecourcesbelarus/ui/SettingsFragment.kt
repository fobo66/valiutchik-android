package fobo66.exchangecourcesbelarus.ui

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.R.xml

class SettingsFragment : PreferenceFragmentCompat() {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(xml.prefs, rootKey)

    val openSourceLicensesPreference = Preference(requireContext())
    openSourceLicensesPreference.title =
      requireContext().getString(R.string.title_activity_oss_licenses)
    openSourceLicensesPreference.setOnPreferenceClickListener {
      findNavController().navigate(R.id.openSourceLicensesFragment)
      true
    }
    preferenceScreen.addPreference(openSourceLicensesPreference)
  }
}
