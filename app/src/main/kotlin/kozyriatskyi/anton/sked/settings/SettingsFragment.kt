package kozyriatskyi.anton.sked.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import kozyriatskyi.anton.sked.R
import kozyriatskyi.anton.sked.data.repository.UserSettingsStorage

/**
 * Created by Anton on 26.08.2017.
 */
class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        val TAG: String = SettingsFragment::class.java.canonicalName!!
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_preferences)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val preference = findPreference<Preference>(key)

        if (preference is ListPreference) {
            preference.summary = preference.entry

            if (key == UserSettingsStorage.KEY_DEFAULT_THEME) {
                val intValue = preference.value.toInt()
                AppCompatDelegate.setDefaultNightMode(intValue)
            }
        }
    }
}