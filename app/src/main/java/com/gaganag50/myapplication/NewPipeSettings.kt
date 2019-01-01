package com.gaganag50.myapplication

import android.content.Context
import android.preference.PreferenceManager

class NewPipeSettings {
    companion object {


        fun initSettings(context: Context) {
            PreferenceManager.setDefaultValues(context, R.xml.appearance_settings, true)
            PreferenceManager.setDefaultValues(context, R.xml.content_settings, true)
            PreferenceManager.setDefaultValues(context, R.xml.main_settings, true)

        }
    }
}
