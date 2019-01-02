package com.gaganag50.myapplication

import android.os.Bundle
import android.support.v7.preference.Preference
import android.util.Log

class AppearanceSettingsFragment : BasePreferenceFragment() {

    /**
     * Theme that was applied when the settings was opened (or recreated after a theme change)
     */
    private var startThemeKey: String? = null

    private val themePreferenceChange = Preference.OnPreferenceChangeListener { preference, newValue ->
        defaultPreferences.edit().putBoolean(Constants.KEY_THEME_CHANGE, true).apply()
        defaultPreferences.edit().putString(getString(R.string.theme_key), newValue.toString()).apply()

        if (newValue != startThemeKey && activity != null) {
            // If it's not the current theme
            activity!!.recreate()
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null)
            super.onCreate(savedInstanceState)
        else
            super.onCreate(null)
        Log.d("MainActivity", "onCreate of appearanceSettingFragment called: ")
        val themeKey = getString(R.string.theme_key)
        startThemeKey = defaultPreferences.getString(themeKey, getString(R.string.default_theme_value))
        findPreference(themeKey).onPreferenceChangeListener = themePreferenceChange


    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.appearance_settings)
    }


}
