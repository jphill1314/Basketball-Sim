package com.appdev.jphil.basketballcoach.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.teams.TeamColor
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.ActivityMainBinding
import com.appdev.jphil.basketballcoach.databinding.NavigationHeaderBinding
import com.appdev.jphil.basketballcoach.util.getStyle
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), NavigationManager {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navBinding: NavigationHeaderBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    var teamViewModel: TeamManagerViewModel? = null

    private var teamTheme = TeamColor.Red
    private var navToRoster = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teamViewModel = getTeamViewModel(viewModelFactory)
        teamViewModel?.currentTeam?.observe(this, Observer<Team> { setTeamNameAndRating(it) })

        savedInstanceState?.let {
            teamTheme = TeamColor.fromInt(savedInstanceState.getInt(TEAM_COLOR))
            setTheme(teamTheme.getStyle())
        }

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        navBinding = NavigationHeaderBinding.bind(binding.navView.getHeaderView(0))

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.frame_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.roster,
                R.id.compose_schedule,
                R.id.standings,
                R.id.rankings,
                R.id.recruiting_compose,
                R.id.strategy,
                R.id.staff,
                R.id.practice
            ),
            binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        if (savedInstanceState != null) {
            navBinding.navTeamName.text = savedInstanceState.getString(TEAM_NAME)
            navBinding.navTeamRating.text = savedInstanceState.getString(TEAM_RATING)
            navBinding.navTeamPrestige.text = savedInstanceState.getString(TEAM_PRESTIGE)

            teamViewModel?.changeTeamAndConference(
                savedInstanceState.getInt(TEAM_ID),
                savedInstanceState.getInt(CONF_ID)
            )

            if (savedInstanceState.getBoolean(NAV_TO_ROSTER, false)) {
                navigateToHomePage()
            }
            navToRoster = false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.frame_layout)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                val nav = findNavController(R.id.frame_layout)
                if (appBarConfiguration.topLevelDestinations.contains(nav.currentDestination?.id)) {
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                } else {
                    nav.popBackStack()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun enableDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun disableDrawer() {
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun navigateToHomePage() {
        findNavController(R.id.frame_layout).popBackStack(R.id.roster, false)
        binding.navView.post { binding.navView.setCheckedItem(R.id.roster) }
    }

    private fun setTeamNameAndRating(team: Team) {
        val oldName = navBinding.navTeamName.text
        navBinding.navTeamName.text = team.name
        navBinding.navTeamRating.text = resources.getString(R.string.rating_colon, team.teamRating)
        navBinding.navTeamPrestige.text = "Prestige: ${team.prestige}"

        binding.navView.menu.apply {
            findItem(R.id.recruiting_compose).isVisible = team.isUser
            findItem(R.id.strategy).isVisible = team.isUser
            findItem(R.id.practice).isVisible = team.isUser
        }

        if (teamTheme != team.color || team.name != oldName) {
            teamTheme = team.color
            navToRoster = true
            recreate()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(TEAM_NAME, navBinding.navTeamName.text.toString())
        outState.putString(TEAM_RATING, navBinding.navTeamRating.text.toString())
        outState.putString(TEAM_PRESTIGE, navBinding.navTeamPrestige.text.toString())
        outState.putInt(TEAM_ID, teamViewModel?.teamId ?: -1)
        outState.putInt(CONF_ID, teamViewModel?.conferenceId ?: 0)
        outState.putInt(TEAM_COLOR, teamTheme.type)
        outState.putBoolean(NAV_TO_ROSTER, navToRoster)
        super.onSaveInstanceState(outState)
    }

    private companion object {
        const val TEAM_NAME = "team name"
        const val TEAM_RATING = "team rating"
        const val TEAM_PRESTIGE = "team prestige"
        const val TEAM_COLOR = "team color"
        const val TEAM_ID = "team id"
        const val CONF_ID = "conf id"
        const val NAV_TO_ROSTER = "nav to roster"
    }
}
