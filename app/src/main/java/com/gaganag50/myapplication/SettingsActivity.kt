package com.gaganag50.myapplication

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem


class SettingsActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    companion object {
        @JvmStatic

        fun initSettings(context: Context) {
            NewPipeSettings.initSettings(context)
        }
    }

    protected override fun onCreate(savedInstanceBundle: Bundle?) {
        setTheme(ThemeHelper.getSettingsThemeStyle(this))

        super.onCreate(savedInstanceBundle)
        setContentView(R.layout.settings_layout)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (savedInstanceBundle == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_holder, MainSettingsFragment())
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val actionBar = getSupportActionBar()
        if (actionBar != null) {
            actionBar!!.setDisplayHomeAsUpEnabled(true)
            actionBar!!.setDisplayShowTitleEnabled(true)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                finish()
            } else
                getSupportFragmentManager().popBackStack()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat?, preference: Preference?): Boolean {
        Log.d("MainActivity", ": ononPreferenceStartFragment called" + caller.toString() + preference)

        val fragment: Fragment = Fragment.instantiate(this, preference!!.getFragment(), preference.getExtras())
        Log.d("MainActivity", "onPreferenceStartFragment: $fragment")

        getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(
                R.animator.custom_fade_in,
                R.animator.custom_fade_out,
                R.animator.custom_fade_in,
                R.animator.custom_fade_out
            )
            .replace(R.id.fragment_holder, fragment)
            .addToBackStack(null)
            .commit()
        Log.d("MainActivity", "onPreferenceStartFragment: ends")

        return true
    }


}


