package com.gaganag50.myapplication

import android.os.Bundle

class MainSettingsFragment : BasePreferenceFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
        addPreferencesFromResource(R.xml.main_settings)

        if (!DEBUG) {
            val debug = findPreference(getString(R.string.debug_pref_screen_key))
            preferenceScreen.removePreference(debug)
        }
    }

    companion object {
        val DEBUG = !BuildConfig.BUILD_TYPE.equals("release")
    }
}
