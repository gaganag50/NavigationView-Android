package com.gaganag50.myapplication

import android.content.Context
import android.preference.PreferenceManager
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes

class ThemeHelper {
    companion object {
        fun resolveResourceIdFromAttr(context: Context, @AttrRes attr: Int): Int {
            val a = context.theme.obtainStyledAttributes(intArrayOf(attr))
            val attributeResourceId = a.getResourceId(0, 0)
            a.recycle()
            return attributeResourceId
        }

        @StyleRes
        fun getSettingsThemeStyle(context: Context): Int {
            val lightTheme = context.resources.getString(R.string.light_theme_key)
            val darkTheme = context.resources.getString(R.string.dark_theme_key)
            val blackTheme = context.resources.getString(R.string.black_theme_key)

            val selectedTheme = getSelectedThemeString(context)

            return if (selectedTheme == lightTheme)
                R.style.LightSettingsTheme
            else if (selectedTheme == blackTheme)
                R.style.BlackSettingsTheme
            else if (selectedTheme == darkTheme)
                R.style.DarkSettingsTheme
            else
                R.style.DarkSettingsTheme// Fallback
        }

        private fun getSelectedThemeString(context: Context): String? {
            val themeKey = context.getString(R.string.theme_key)
            val defaultTheme = context.resources.getString(R.string.default_theme_value)
            return PreferenceManager.getDefaultSharedPreferences(context).getString(themeKey, defaultTheme)
        }

        @StyleRes
        fun getThemeForService(context: Context, serviceId: Int): Int {
            val lightTheme = context.resources.getString(R.string.light_theme_key)
            val darkTheme = context.resources.getString(R.string.dark_theme_key)
            val blackTheme = context.resources.getString(R.string.black_theme_key)

            val selectedTheme = getSelectedThemeString(context)

            var defaultTheme = R.style.DarkTheme
            if (selectedTheme == lightTheme)
                defaultTheme = R.style.LightTheme
            else if (selectedTheme == blackTheme)
                defaultTheme = R.style.BlackTheme
            else if (selectedTheme == darkTheme) defaultTheme = R.style.DarkTheme


            return defaultTheme


        }

        fun setTheme(context: Context, serviceId: Int) {
            context.setTheme(getThemeForService(context, serviceId))

        }
    }

}
