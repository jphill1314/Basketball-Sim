package com.appdev.jphil.basketballcoach.main

import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.coaches.CoachesFragment
import com.appdev.jphil.basketballcoach.practice.PracticeFragment
import com.appdev.jphil.basketballcoach.recruiting.RecruitFragment
import com.appdev.jphil.basketballcoach.roster.RosterFragment
import com.appdev.jphil.basketballcoach.schedule.ScheduleFragment
import com.appdev.jphil.basketballcoach.standings.StandingsFragment
import com.appdev.jphil.basketballcoach.strategy.StrategyFragment
import com.appdev.jphil.basketballcoach.tracking.TrackingKeys
import com.flurry.android.FlurryAgent
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity(), NavigationManager {

    private var drawerLayout: DrawerLayout? = null
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        enableNavigation()

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener { menuItem -> handleFragmentNavigation(menuItem, false) }
        if (savedInstanceState == null) {
            navigateToHomePage(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout?.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleFragmentNavigation(menuItem: MenuItem, isStartup: Boolean): Boolean {
        while (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        }

        val fragment: Fragment? = when (menuItem.itemId) {
            R.id.roster -> RosterFragment()
            R.id.schedule -> ScheduleFragment()
            R.id.standings -> StandingsFragment()
            R.id.recruiting -> RecruitFragment()
            R.id.strategy -> StrategyFragment()
            R.id.staff -> CoachesFragment()
            R.id.practice -> PracticeFragment()
            else -> null
        }

        drawerLayout?.closeDrawers()
        if (fragment != null && !menuItem.isChecked) {
            FlurryAgent.logEvent(TrackingKeys.EVENT_VIEW_SCREEN, mutableMapOf(TrackingKeys.PAYLOAD_SCREEN_NAME to fragment::class.java.simpleName))
            menuItem.isChecked = true
            while (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStackImmediate()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit()
            return true
        }
        return false
    }

    override fun disableNavigation() {
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.setHomeAsUpIndicator(null)
        }
    }

    override fun enableNavigation() {
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }
    }

    override fun navigateToHomePage() {
        navigateToHomePage(false)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0 && !isOnHomepage()) {
            navigateToHomePage()
        } else {
            super.onBackPressed()
        }
    }

    private fun navigateToHomePage(isStartup: Boolean) {
        handleFragmentNavigation(navView.menu.getItem(0), isStartup)
    }

    private fun isOnHomepage(): Boolean = navView.menu.getItem(0).isChecked
}
