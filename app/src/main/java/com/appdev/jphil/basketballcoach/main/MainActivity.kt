package com.appdev.jphil.basketballcoach.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.MenuItem
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.roster.RosterFragment
import com.appdev.jphil.basketballcoach.schedule.ScheduleFragment
import com.appdev.jphil.basketballcoach.standings.StandingsFragment
import com.appdev.jphil.basketballcoach.strategy.StrategyFragment
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : DaggerAppCompatActivity(), ChangeTeamConfContract.Listener {

    private lateinit var drawerLayout: DrawerLayout
    private var teamId = DEFAULT_TEAM_ID
    private var conferenceId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.let {
            teamId = it.getInt(TEAM_ID_STRING, 1)
            conferenceId = it.getInt(CONF_ID_STRING, 1)
        }

        setSupportActionBar(toolbar)
        supportActionBar.let {
            it?.setDisplayHomeAsUpEnabled(true)
            it?.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }

        drawerLayout = drawer_layout
        nav_view.setNavigationItemSelectedListener { menuItem -> handleFragmentNavigation(menuItem) }
        if (savedInstanceState == null) {
            handleFragmentNavigation(nav_view.menu.getItem(0))
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt(TEAM_ID_STRING, teamId)
        outState?.putInt(CONF_ID_STRING, conferenceId)
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleFragmentNavigation(menuItem: MenuItem): Boolean {
        val fragment: Fragment? = when (menuItem.itemId) {
            R.id.roster -> RosterFragment.newInstance(teamId)
            R.id.schedule -> ScheduleFragment.newInstance(teamId)
            R.id.standings -> StandingsFragment.newInstance(teamId, conferenceId)
            R.id.recruiting -> null
            R.id.strategy -> StrategyFragment.newInstance(teamId)
            R.id.staff -> null
            R.id.training -> null
            else -> null
        }

        if (fragment != null && !menuItem.isChecked) {
            menuItem.isChecked = true
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit()
        }

        drawerLayout.closeDrawers()
        return true
    }

    override fun changeTeam(teamId: Int) {
        this.teamId = teamId
    }

    override fun changeConference(conferenceId: Int, teamId: Int) {
        this.conferenceId = conferenceId
        this.teamId = teamId
    }

    companion object {
        private const val TEAM_ID_STRING = "teamId"
        private const val CONF_ID_STRING = "confId"
        const val DEFAULT_TEAM_ID = -1
    }
}
