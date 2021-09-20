package com.appdev.jphil.basketballcoach.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.ActivityMainBinding
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), NavigationManager {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    var teamViewModel: TeamManagerViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teamViewModel = getTeamViewModel(viewModelFactory)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frame_layout) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.start_screen -> binding.bottomNav.visibility = View.GONE
                R.id.customize -> binding.bottomNav.visibility = View.GONE
                R.id.new_game -> binding.bottomNav.visibility = View.GONE
                R.id.new_season -> binding.bottomNav.visibility = View.GONE
                R.id.gameFragment -> binding.bottomNav.visibility = View.GONE
                else -> binding.bottomNav.visibility = View.VISIBLE
            }
        }

        if (savedInstanceState != null) {
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
                nav.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun hideToolbar() {
        binding.toolbar.visibility = View.GONE
    }

    override fun showToolbar() {
        binding.toolbar.visibility = View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(TEAM_ID, teamViewModel?.teamId ?: -1)
        outState.putInt(CONF_ID, teamViewModel?.conferenceId ?: 0)
        super.onSaveInstanceState(outState)
    }

    private companion object {
        const val TEAM_ID = "team id"
        const val CONF_ID = "conf id"
    }
}
