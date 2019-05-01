package com.appdev.jphil.basketballcoach.main

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.MenuItem
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
import javax.inject.Inject

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
        navView.setNavigationItemSelectedListener { menuItem -> handleFragmentNavigation(menuItem) }
        if (savedInstanceState == null) {
            navigateToHomePage()
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

    private fun handleFragmentNavigation(menuItem: MenuItem): Boolean {
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
            R.id.training -> PracticeFragment()
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
        handleFragmentNavigation(navView.menu.getItem(0))
    }
}
