package fobo66.exchangecourcesbelarus.ui;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import fobo66.exchangecourcesbelarus.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_general, rootKey);
    }
}
