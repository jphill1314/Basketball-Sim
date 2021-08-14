package com.appdev.jphil.basketballcoach.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeamManagerViewModel(private val database: BasketballDatabase) : ViewModel() {

    var teamId = -1
    var conferenceId = 0

    fun changeTeamAndConference(teamId: Int, conferenceId: Int) {
        this.teamId = teamId
        this.conferenceId = conferenceId
        loadTeam()
    }

    private val team = MutableLiveData<Team>()
    val currentTeam: LiveData<Team> = team

    private fun loadTeam() {
        GlobalScope.launch(Dispatchers.Main) {
            var team: Team? = null
            launch(Dispatchers.IO) {
                team = if (teamId == -1) {
                    TeamDatabaseHelper.loadUserTeam(database)
                } else {
                    TeamDatabaseHelper.loadTeamById(teamId, database)
                }
            }.join()
            team?.let { this@TeamManagerViewModel.team.value = team }
        }
    }
}
