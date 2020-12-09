package com.appdev.jphil.basketballcoach.practice

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PracticeRepository @Inject constructor(
    @TeamId private val teamId: Int,
    private val database: BasketballDatabase
) : PracticeContract.Repository {

    private lateinit var presenter: PracticeContract.Presenter

    override suspend fun loadTeam(): Team {
        return TeamDatabaseHelper.loadTeamById(teamId, database) ?: throw IllegalStateException(
            "Not team exists with teamId = $teamId"
        )
    }

    override suspend fun saveTeam(team: Team) {
        GlobalScope.launch(Dispatchers.IO) {
            TeamDatabaseHelper.saveTeam(team, database)
        }
    }

    override fun attachPresenter(presenter: PracticeContract.Presenter) {
        this.presenter = presenter
    }
}
