package com.appdev.jphil.basketballcoach.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.team.TeamEntity
import kotlinx.coroutines.launch

class TeamManagerViewModel(private val database: BasketballDatabase) : ViewModel() {

    var teamId = -1
    var conferenceId = 0

    fun changeTeamAndConference(teamId: Int, conferenceId: Int) {
        this.teamId = teamId
        this.conferenceId = conferenceId
        loadTeam()
    }

    private val team = MutableLiveData<TeamEntity>()
    val currentTeam: LiveData<TeamEntity> = team

    private fun loadTeam() {
        viewModelScope.launch {
            team.value = if (teamId == -1) {
                database.teamDao().getTeamIsUser(true)
            } else {
                database.teamDao().getTeamWithId(teamId)
            }
        }
    }
}
