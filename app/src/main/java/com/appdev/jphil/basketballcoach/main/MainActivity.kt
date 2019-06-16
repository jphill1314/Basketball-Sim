package com.appdev.jphil.basketballcoach.main

import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import android.view.MenuItem
import android.widget.TextView
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
    private lateinit var teamName: TextView
    private lateinit var teamRating: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        enableNavigation()

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener { menuItem -> handleFragmentNavigation(menuItem) }
        navView.getHeaderView(0)?.apply {
            teamName = findViewById(R.id.nav_team_name)
            teamRating = findViewById(R.id.nav_team_rating)
        }

        if (savedInstanceState == null) {
            navigateToHomePage()
        } else {
            teamName.text = savedInstanceState.getString(TEAM_NAME)
            teamRating.text = savedInstanceState.getString(TEAM_RATING)
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
        handleFragmentNavigation(navView.menu.getItem(0))
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0 && !isOnHomepage()) {
            navigateToHomePage()
        } else {
            super.onBackPressed()
        }
    }

    override fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    private fun isOnHomepage(): Boolean = navView.menu.getItem(0).isChecked

    override fun setTeamNameAndRating(name: String, rating: Int) {
        teamName.text = name
        teamRating.text = resources.getString(R.string.rating_colon, rating)
        // TODO: change this so that the activity does its own db call -> this will allow more complex data in the header
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(TEAM_NAME, teamName.text.toString())
        outState.putString(TEAM_RATING, teamRating.text.toString())
        super.onSaveInstanceState(outState)
    }

    private companion object {
        const val TEAM_NAME = "team name"
        const val TEAM_RATING = "team rating"
    }
}
