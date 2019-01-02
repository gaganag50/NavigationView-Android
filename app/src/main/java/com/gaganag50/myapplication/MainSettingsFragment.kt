package com.gaganag50.myapplication

import android.os.Bundle
import android.support.v7.preference.Preference

class MainSettingsFragment : BasePreferenceFragment() {


    companion object {
        val DEBUG = !BuildConfig.BUILD_TYPE.equals("release")

    }

    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.main_settings)
        if(MainSettingsFragment.DEBUG == false){
            val debug: Preference = findPreference(getString(R.string.debug_pref_screen_key))

            preferenceScreen.removePreference(debug)
        }
    }
}
