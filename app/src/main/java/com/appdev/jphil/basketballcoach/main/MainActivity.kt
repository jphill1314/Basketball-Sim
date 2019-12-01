package com.appdev.jphil.basketballcoach.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.GravityCompat
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.teams.TeamColor
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.ActivityMainBinding
import com.appdev.jphil.basketballcoach.databinding.NavigationHeaderBinding
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

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
            setTheme(getStyle(teamTheme))
        }

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        navBinding = NavigationHeaderBinding.bind(binding.navView.getHeaderView(0))

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
//        enableNavigation()

        val navController = findNavController(R.id.frame_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.roster,
                R.id.schedule,
                R.id.standings,
                R.id.recruiting,
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

//    override fun disableNavigation() {
//        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
//        supportActionBar?.let {
//            it.setDisplayHomeAsUpEnabled(false)
//            it.setHomeAsUpIndicator(null)
//        }
//    }
//
//    override fun enableNavigation() {
//        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
//        supportActionBar?.let {
//            it.setDisplayHomeAsUpEnabled(true)
//            it.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
//        }
//    }

    private fun navigateToHomePage() {
        findNavController(R.id.frame_layout).popBackStack(R.id.roster, false)
        binding.navView.post { binding.navView.setCheckedItem(R.id.roster) }
    }

    private fun setTeamNameAndRating(team: Team) {
        navBinding.navTeamName.text = team.name
        navBinding.navTeamRating.text = resources.getString(R.string.rating_colon, team.teamRating)

        binding.navView.menu.apply {
            findItem(R.id.recruiting).isVisible = team.isUser
            findItem(R.id.strategy).isVisible = team.isUser
            findItem(R.id.practice).isVisible = team.isUser
        }

        if (teamTheme != team.color) {
            teamTheme = team.color
            navToRoster = true
            recreate()
        }
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
        outState.putString(TEAM_NAME, navBinding.navTeamName.text.toString())
        outState.putString(TEAM_RATING, navBinding.navTeamRating.text.toString())
        outState.putInt(TEAM_ID, teamViewModel?.teamId ?: -1)
        outState.putInt(CONF_ID, teamViewModel?.conferenceId ?: 0)
        outState.putInt(TEAM_COLOR, teamTheme.type)
        outState.putBoolean(NAV_TO_ROSTER, navToRoster)
        super.onSaveInstanceState(outState)
    }

    private companion object {
        const val TEAM_NAME = "team name"
        const val TEAM_RATING = "team rating"
        const val TEAM_COLOR = "team color"
        const val TEAM_ID = "team id"
        const val CONF_ID = "conf id"
        const val NAV_TO_ROSTER = "nav to roster"
    }
}
