package com.gaganag50.myapplication


import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.View


abstract class BasePreferenceFragment : PreferenceFragmentCompat() {
    protected val TAG = javaClass.getSimpleName() + "@" + Integer.toHexString(hashCode())
    protected val DEBUG = MainActivity.DEBUG

    protected lateinit var defaultPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        defaultPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity())
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDivider(null)
        updateTitle()
    }

    override fun onResume() {
        super.onResume()
        updateTitle()
    }

    private fun updateTitle() {
        if (getActivity() is AppCompatActivity) {
            val actionBar = (getActivity() as AppCompatActivity).supportActionBar
            actionBar?.setTitle(getPreferenceScreen().getTitle())
        }
    }
}