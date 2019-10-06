package com.appdev.jphil.basketballcoach.main

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import android.view.MenuItem
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.teams.TeamColor
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.coaches.CoachesFragment
import com.appdev.jphil.basketballcoach.databinding.ActivityMainBinding
import com.appdev.jphil.basketballcoach.practice.PracticeFragment
import com.appdev.jphil.basketballcoach.recruiting.RecruitFragment
import com.appdev.jphil.basketballcoach.roster.RosterFragment
import com.appdev.jphil.basketballcoach.schedule.ScheduleFragment
import com.appdev.jphil.basketballcoach.standings.StandingsFragment
import com.appdev.jphil.basketballcoach.strategy.StrategyFragment
import com.appdev.jphil.basketballcoach.tracking.TrackingKeys
import com.flurry.android.FlurryAgent
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), NavigationManager {

    private lateinit var binding: ActivityMainBinding
    private lateinit var teamName: TextView
    private lateinit var teamRating: TextView

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    var teamViewModel: TeamManagerViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)
        teamViewModel = getTeamViewModel(viewModelFactory)
        teamViewModel?.currentTeam?.observe(this, Observer<Team> { setTeamNameAndRating(it) })

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))
        enableNavigation()

        binding.navView.setNavigationItemSelectedListener { menuItem -> handleFragmentNavigation(menuItem) }
        binding.navView.getHeaderView(0)?.apply {
            teamName = findViewById(R.id.nav_team_name)
            teamRating = findViewById(R.id.nav_team_rating)
        }

        if (savedInstanceState == null) {
            navigateToHomePage()
        } else {
            teamName.text = savedInstanceState.getString(TEAM_NAME)
            teamRating.text = savedInstanceState.getString(TEAM_RATING)

            teamViewModel?.changeTeamAndConference(
                savedInstanceState.getInt(TEAM_ID),
                savedInstanceState.getInt(CONF_ID)
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
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

        binding.drawerLayout.closeDrawers()
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
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.setHomeAsUpIndicator(null)
        }
    }

    override fun enableNavigation() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }
    }

    override fun navigateToHomePage() {
        handleFragmentNavigation(binding.navView.menu.getItem(0))
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

    private fun isOnHomepage(): Boolean = binding.navView.menu.getItem(0).isChecked

    private fun setTeamNameAndRating(team: Team) {
        teamName.text = team.name
        teamRating.text = resources.getString(R.string.rating_colon, team.teamRating)

        binding.navView.menu.apply {
            findItem(R.id.recruiting).isVisible = team.isUser
            findItem(R.id.strategy).isVisible = team.isUser
            findItem(R.id.practice).isVisible = team.isUser
        }

        // TODO: this whole business needs a lot of work
        setTheme(getStyle(team.color))
        val value = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimary, value, true)
        binding.toolbar.background = ColorDrawable(value.data)

        theme.resolveAttribute(R.attr.colorPrimaryDark, value, true)
        window.statusBarColor = value.data
    }

    private fun getStyle(color: TeamColor) = when (color) {
        TeamColor.Red -> R.style.AppTheme_Red
        TeamColor.Pink -> R.style.AppTheme_Pink
        TeamColor.Purple -> R.style.AppTheme_Purple
        TeamColor.DeepPurple -> R.style.AppTheme_DeepPurple
        TeamColor.Indigo -> R.style.AppTheme_Indigo
        TeamColor.Blue -> R.style.AppTheme_Blue
        TeamColor.LightBlue -> R.style.AppTheme_LightBlue
        TeamColor.Cyan -> R.style.AppTheme_Cyan
        TeamColor.Teal -> R.style.AppTheme_Teal
        TeamColor.Green -> R.style.AppTheme_Green
        TeamColor.LightGreen -> R.style.AppTheme_LightGreen
        TeamColor.Yellow -> R.style.AppTheme_Yellow
        TeamColor.Orange -> R.style.AppTheme_Orange
        TeamColor.DeepOrange -> R.style.AppTheme_DeepOrange
        TeamColor.BlueGrey -> R.style.AppTheme_BlueGrey
        else -> R.style.AppTheme_Red
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(TEAM_NAME, teamName.text.toString())
        outState.putString(TEAM_RATING, teamRating.text.toString())
        outState.putInt(TEAM_ID, teamViewModel?.teamId ?: -1)
        outState.putInt(CONF_ID, teamViewModel?.conferenceId ?: 0)
        super.onSaveInstanceState(outState)
    }

    private companion object {
        const val TEAM_NAME = "team name"
        const val TEAM_RATING = "team rating"
        const val TEAM_ID = "team id"
        const val CONF_ID = "conf id"
    }
}
