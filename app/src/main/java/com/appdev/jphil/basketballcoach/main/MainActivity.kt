package com.appdev.jphil.basketballcoach.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.team.TeamEntity
import com.appdev.jphil.basketballcoach.databinding.ActivityMainBinding
import com.appdev.jphil.basketballcoach.databinding.NavigationHeaderBinding
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), NavigationManager {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navBinding: NavigationHeaderBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    var teamViewModel: TeamManagerViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teamViewModel = getTeamViewModel(viewModelFactory)
        teamViewModel?.currentTeam?.observe(this) { setTeamNameAndRating(it) }

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        navBinding = NavigationHeaderBinding.bind(binding.navView.getHeaderView(0))

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.frame_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.team,
                R.id.compose_schedule,
                R.id.standings,
                R.id.rankings,
                R.id.recruiting_compose,
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

    override fun hideToolbar() {
        binding.toolbar.visibility = View.GONE
    }

    override fun showToolbar() {
        binding.toolbar.visibility = View.VISIBLE
    }

    private fun setTeamNameAndRating(team: TeamEntity) {
        navBinding.navTeamName.text = team.schoolName + " " + team.mascot
        navBinding.navTeamRating.text = resources.getString(R.string.rating_colon, team.rating)
        navBinding.navTeamPrestige.text = "Prestige: ${team.prestige}"

        binding.navView.menu.apply {
            findItem(R.id.recruiting_compose).isVisible = team.isUser
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(TEAM_NAME, navBinding.navTeamName.text.toString())
        outState.putString(TEAM_RATING, navBinding.navTeamRating.text.toString())
        outState.putString(TEAM_PRESTIGE, navBinding.navTeamPrestige.text.toString())
        outState.putInt(TEAM_ID, teamViewModel?.teamId ?: -1)
        outState.putInt(CONF_ID, teamViewModel?.conferenceId ?: 0)
        super.onSaveInstanceState(outState)
    }

    private companion object {
        const val TEAM_NAME = "team name"
        const val TEAM_RATING = "team rating"
        const val TEAM_PRESTIGE = "team prestige"
        const val TEAM_ID = "team id"
        const val CONF_ID = "conf id"
    }
}
