package com.gaganag50.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import com.nostra13.universalimageloader.core.ImageLoader

class NavigationHelper {
    companion object {
        val MAIN_FRAGMENT_TAG = "main_fragment_tag"
        val SEARCH_FRAGMENT_TAG = "search_fragment_tag"
        val TAG = "NavigationHelper"
        @SuppressLint("CommitTransaction")
        private fun defaultTransaction(fragmentManager: FragmentManager): FragmentTransaction {
            return fragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.animator.custom_fade_in,
                    R.animator.custom_fade_out,
                    R.animator.custom_fade_in,
                    R.animator.custom_fade_out
                )
        }

        fun openSubscriptionFragment(fragmentManager: FragmentManager) {
            defaultTransaction(fragmentManager)
                .replace(R.id.fragment_holder, SubscriptionFragment())
                .addToBackStack(null)
                .commit()
        }

        fun openSettings(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }

        fun openAbout(context: Context) {
            val intent = Intent(context, AboutActivity::class.java)
            context.startActivity(intent)
        }

        fun openWhatsNewFragment(fragmentManager: FragmentManager?) {
            defaultTransaction(fragmentManager!!)
                .replace(R.id.fragment_holder, FeedFragment())
                .addToBackStack(null)
                .commit()
        }

        fun openBookmarksFragment(fragmentManager: FragmentManager) {
            defaultTransaction(fragmentManager)
                .replace(R.id.fragment_holder, BookmarkFragment())
                .addToBackStack(null)
                .commit()
        }

        fun openDownloads(activity: MainActivity): Boolean {
            if (!PermissionHelper.checkStoragePermissions(activity, PermissionHelper.DOWNLOADS_REQUEST_CODE)) {
                return false
            }
            val intent = Intent(activity, DownloadActivity::class.java)
            activity.startActivity(intent)
            return true

        }

        fun openStatisticFragment(fragmentManager: FragmentManager?) {
            defaultTransaction(fragmentManager!!)
                .replace(R.id.fragment_holder, StatisticsPlaylistFragment())
                .addToBackStack(null)
                .commit()
        }

        fun openMainActivity(activity: MainActivity): Boolean {
            if (!PermissionHelper.checkStoragePermissions(activity, PermissionHelper.DOWNLOADS_REQUEST_CODE)) {
                return false
            }
            val intent = Intent(activity, DownloadActivity::class.java)
            activity.startActivity(intent)
            return true
        }

        fun tryGotoSearchFragment(fragmentManager: FragmentManager?): Any {
            if (MainActivity.DEBUG) {
                for (i in 0 until fragmentManager!!.getBackStackEntryCount()) {
                    Log.d(
                        "NavigationHelper",
                        "tryGoToSearchFragment() [" + i + "] = [" + fragmentManager.getBackStackEntryAt(i) + "]"
                    )
                }
            }

            return fragmentManager!!.popBackStackImmediate(SEARCH_FRAGMENT_TAG, 0)

        }

        fun gotoMainFragment(fragmentManager: FragmentManager?) {
            ImageLoader.getInstance().clearMemoryCache()

            val popped = fragmentManager!!.popBackStackImmediate(MAIN_FRAGMENT_TAG, 0)
            if (!popped) openMainFragment(fragmentManager)
        }

        private fun openMainFragment(fragmentManager: FragmentManager) {
            Log.d(TAG, ":onMainFragment called ")
        }


    }


}
