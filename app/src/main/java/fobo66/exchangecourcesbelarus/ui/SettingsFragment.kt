package fobo66.exchangecourcesbelarus.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import fobo66.exchangecourcesbelarus.R.xml

class SettingsFragment : PreferenceFragmentCompat() {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(xml.prefs, rootKey)
  }
}