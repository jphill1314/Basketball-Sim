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

    override fun loadTeam() {
        GlobalScope.launch(Dispatchers.IO) {
            val team = TeamDatabaseHelper.loadTeamById(teamId, database)
            if (team != null) {
                withContext(Dispatchers.Main) {
                    presenter.onTeamLoaded(team)
                }
            }
        }
    }

    override fun saveTeam(team: Team) {
        GlobalScope.launch(Dispatchers.IO) {
            TeamDatabaseHelper.saveTeam(team, database)
        }
    }

    override fun attachPresenter(presenter: PracticeContract.Presenter) {
        this.presenter = presenter
    }
}