package com.appdev.jphil.basketballcoach.roster

import android.content.res.Resources
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import com.appdev.jphil.basketballcoach.newseason.NewGameGenerator
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RosterRepository @Inject constructor(
    @TeamId private val lazyTeamId: Lazy<Int>,
    private val database: BasketballDatabase,
    private val resources: Resources
): RosterContract.Repository {

    private lateinit var presenter: RosterContract.Presenter

    override suspend fun fetchData(): Team {
        val teamId = lazyTeamId.get()
        var team = if (teamId == -1) {
            TeamDatabaseHelper.loadUserTeam(database)
        } else {
            TeamDatabaseHelper.loadTeamById(teamId, database)
        }

        if (team == null) {
            NewGameGenerator.generateNewGame(resources, database)
            team = TeamDatabaseHelper.loadUserTeam(database)
        }
        return team!!
    }

    override suspend fun saveTeam(team: Team) {
        TeamDatabaseHelper.saveTeam(team, database)
    }

    override fun attachPresenter(presenter: RosterContract.Presenter) {
        this.presenter = presenter
    }
}
