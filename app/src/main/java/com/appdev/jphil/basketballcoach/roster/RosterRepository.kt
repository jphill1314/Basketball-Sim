package com.appdev.jphil.basketballcoach.roster

import android.content.res.Resources
import com.appdev.jphil.basketball.factories.BasketballFactory
import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketball.factories.TeamFactory
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import com.appdev.jphil.basketballcoach.main.MainActivity
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RosterRepository @Inject constructor(
    @TeamId private val teamId: Int,
    private val database: BasketballDatabase,
    private val resources: Resources
): RosterContract.Repository {

    private lateinit var presenter: RosterContract.Presenter

    override fun fetchData() {
        GlobalScope.launch(Dispatchers.IO) {
            var team = if (teamId == MainActivity.DEFAULT_TEAM_ID) {
                TeamDatabaseHelper.loadUserTeam(database)
            } else {
                TeamDatabaseHelper.loadTeamById(teamId, database)
            }

            if (team == null) {
                createGame()
                team = TeamDatabaseHelper.loadUserTeam(database)
            }
            withContext(Dispatchers.Main) { presenter.onDataFetched(team!!) }
        }
    }

    override fun saveTeam(team: Team) {
        GlobalScope.launch(Dispatchers.IO) {
            TeamDatabaseHelper.saveTeam(team, database)
        }
    }

    private fun createGame() {
        val world = BasketballFactory.setupWholeBasketballWorld(
            resources.getStringArray(R.array.first_names).asList(),
            resources.getStringArray(R.array.last_names).asList()
        )
        world.conferences.forEach {
            ConferenceDatabaseHelper.saveConference(it, database)
            GameDatabaseHelper.saveOnlyGames(it.generateSchedule(2018), database)
        }
        RecruitDatabaseHelper.saveRecruits(world.recruits, database)
    }

    override fun attachPresenter(presenter: RosterContract.Presenter) {
        this.presenter = presenter
    }
}