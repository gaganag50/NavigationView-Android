package com.gaganag50.myapplication

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    companion object {
        val DEBUG = !BuildConfig.BUILD_TYPE.equals("release")

    }

    private var drawer: DrawerLayout? = null
    private var drawerItems: NavigationView? = null
    private lateinit var mDrawerLayout: DrawerLayout


    private var serviceArrow: ImageView? = null
    private var headerServiceView: TextView? = null


    private val ITEM_ID_SUBSCRIPTIONS = -1
    private val ITEM_ID_FEED = -2
    private val ITEM_ID_BOOKMARKS = -3
    private val ITEM_ID_DOWNLOADS = -4
    private val ITEM_ID_HISTORY = -5
    private val ITEM_ID_SETTINGS = 0
    private val ITEM_ID_ABOUT = 1

    private val ORDER = 0
    private var toggle: ActionBarDrawerToggle? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        if (DEBUG) Log.d(TAG, "onCreate() called with: savedInstanceState = [$savedInstanceState]")
//        ThemeHelper.setTheme(this, ServiceHelper.getSelectedServiceId(this))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        try {
            setupDrawer()
        } catch (e: Exception) {
            ErrorActivity.reportUiError(this, e)
        }

    }


    // there is nothing  which throw Exception
    // but it is only for learning how to handle exceptions
    @Throws(Exception::class)
    private fun setupDrawer() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        drawer = findViewById(R.id.drawer_layout)
        drawerItems = findViewById(R.id.navigation)

        drawerItems!!.getMenu()
            .add(R.id.menu_tabs_group, ITEM_ID_SUBSCRIPTIONS, ORDER, R.string.tab_subscriptions)
            .setIcon(ThemeHelper.resolveResourceIdFromAttr(this, R.attr.ic_channel))
        drawerItems!!.getMenu()
            .add(R.id.menu_tabs_group, ITEM_ID_FEED, ORDER, R.string.fragment_whats_new)
            .setIcon(ThemeHelper.resolveResourceIdFromAttr(this, R.attr.rss))
        drawerItems!!.getMenu()
            .add(R.id.menu_tabs_group, ITEM_ID_BOOKMARKS, ORDER, R.string.tab_bookmarks)
            .setIcon(ThemeHelper.resolveResourceIdFromAttr(this, R.attr.ic_bookmark))
        drawerItems!!.getMenu()
            .add(R.id.menu_tabs_group, ITEM_ID_DOWNLOADS, ORDER, R.string.downloads)
            .setIcon(ThemeHelper.resolveResourceIdFromAttr(this, R.attr.download))
        drawerItems!!.getMenu()
            .add(R.id.menu_tabs_group, ITEM_ID_HISTORY, ORDER, R.string.action_history)
            .setIcon(ThemeHelper.resolveResourceIdFromAttr(this, R.attr.history))

        //Settings and About
        drawerItems!!.getMenu()
            .add(R.id.menu_options_about_group, ITEM_ID_SETTINGS, ORDER, R.string.settings)
            .setIcon(ThemeHelper.resolveResourceIdFromAttr(this, R.attr.settings))
        drawerItems!!.getMenu()
            .add(R.id.menu_options_about_group, ITEM_ID_ABOUT, ORDER, R.string.tab_about)
            .setIcon(ThemeHelper.resolveResourceIdFromAttr(this, R.attr.info))

        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close)
        toggle!!.syncState()
        drawer!!.addDrawerListener(toggle!!)


        drawerItems!!.setNavigationItemSelectedListener {
            drawerItemSelected(it)

        }
        setupDrawerHeader()

    }

    private fun drawerItemSelected(item: MenuItem): Boolean {
        when (item.groupId) {
            R.id.menu_tabs_group -> try {
                tabSelected(item)
            } catch (e: Exception) {
                ErrorActivity.reportUiError(this, e)

            }

            R.id.menu_options_about_group -> optionsAboutSelected(item)
            else -> return false
        }

        drawer?.closeDrawers()
        return true
    }


    private fun tabSelected(item: MenuItem) {
        when (item.itemId) {
            ITEM_ID_SUBSCRIPTIONS -> NavigationHelper.openSubscriptionFragment(supportFragmentManager)
            ITEM_ID_FEED -> NavigationHelper.openWhatsNewFragment(supportFragmentManager)
            ITEM_ID_BOOKMARKS -> NavigationHelper.openBookmarksFragment(supportFragmentManager)
            ITEM_ID_DOWNLOADS -> NavigationHelper.openDownloads(this)
            ITEM_ID_HISTORY -> NavigationHelper.openStatisticFragment(supportFragmentManager)

        }
    }

    private fun optionsAboutSelected(item: MenuItem) {
        when (item.itemId) {
            ITEM_ID_SETTINGS -> NavigationHelper.openSettings(this)
            ITEM_ID_ABOUT -> NavigationHelper.openAbout(this)
        }
    }

    private fun setupDrawerHeader() {
        val navigationView: NavigationView = findViewById(R.id.navigation)
        val hView = navigationView.getHeaderView(0)

        serviceArrow = hView.findViewById(R.id.drawer_arrow)
        headerServiceView = hView.findViewById(R.id.drawer_header_service_view)
        val action: Button = hView.findViewById(R.id.drawer_header_action_button)
        action.setOnClickListener({ view -> toggleServices() })
    }

    private fun toggleServices() {
        Log.d("MainActivity", ": toggleServices")

    }


    override fun onResume() {
        super.onResume()

        // close drawer on return, and don't show animation, so its looks like the drawer isn't open
        // when the user returns to MainActivity
        drawer!!.closeDrawer(Gravity.START, false)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (sharedPreferences.getBoolean(Constants.KEY_THEME_CHANGE, false)) {
            Log.d(TAG, "Theme has changed, recreating activity...")
            sharedPreferences.edit().putBoolean(Constants.KEY_THEME_CHANGE, false).apply()
            // https://stackoverflow.com/questions/10844112/runtimeexception-performing-pause-of-activity-that-is-not-resumed
            // Briefly, let the activity resume properly posting the recreate call to end of the message queue
            Handler(Looper.getMainLooper()).post { this@MainActivity.recreate() }
        }

        if (sharedPreferences.getBoolean(Constants.KEY_MAIN_PAGE_CHANGE, false)) {
            Log.d(TAG, "main page has changed, recreating main fragment...")
            sharedPreferences.edit().putBoolean(Constants.KEY_MAIN_PAGE_CHANGE, false).apply()
            NavigationHelper.openMainActivity(this)
        }
    }

    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed() called")

        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_holder)
        // If current fragment implements BackPressable (i.e. can/wanna handle back press) delegate the back press to it
        if (fragment is BackPressable) {
            if ((fragment as BackPressable).onBackPressed()) return
        }


        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else
            super.onBackPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        for (i in grantResults) {
            if (i == PackageManager.PERMISSION_DENIED) {
                return
            }
        }
        when (requestCode) {
            PermissionHelper.DOWNLOADS_REQUEST_CODE -> NavigationHelper.openDownloads(this)
            PermissionHelper.DOWNLOAD_DIALOG_REQUEST_CODE -> {
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_holder)
                if (fragment is VideoDetailFragment) {
                    Log.d(TAG, ": VideoDetailFragment is called ")
                }
            }
        }
    }

    private fun onHomeButtonPressed() {

        // If search fragment wasn't found in the backstack...
        if (NavigationHelper.tryGotoSearchFragment(supportFragmentManager) == false) {
            // ...go to the main fragment
            NavigationHelper.gotoMainFragment(supportFragmentManager)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (DEBUG) Log.d(TAG, "onCreateOptionsMenu() called with: menu = [$menu]")
        super.onCreateOptionsMenu(menu)

        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_holder)
        if (fragment !is VideoDetailFragment) {
            val viewById = findViewById<Toolbar>(R.id.toolbar).findViewById<View>(R.id.toolbar_search_container)

            findViewById<Toolbar>(R.id.toolbar).findViewById<View>(R.id.toolbar_spinner).setVisibility(View.GONE)
        }


        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        updateDrawerNavigation()

        return true
    }

    private fun updateDrawerNavigation() {

        if (supportActionBar == null) return

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)

        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_holder)
//        if (fragment is MainFragment) {
//            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
//            if (toggle != null) {
//                toggle.syncState()
//                toolbar.setNavigationOnClickListener({ v -> drawer.openDrawer(GravityCompat.START) })
//                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED)
//            }
//        } else {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener({ v -> onHomeButtonPressed() })
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (DEBUG) Log.d(TAG, "onOptionsItemSelected() called with: item = [$item]")
        val id = item.itemId

        when (id) {
            android.R.id.home -> {
                onHomeButtonPressed()
                return true
            }
            R.id.action_show_downloads -> return NavigationHelper.openDownloads(this)
            R.id.action_history -> {
                NavigationHelper.openStatisticFragment(supportFragmentManager)
                return true
            }
            R.id.action_settings -> {
                NavigationHelper.openSettings(this)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


}
