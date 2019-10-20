package com.appdev.jphil.basketballcoach.roster

import android.content.res.Resources
import com.appdev.jphil.basketball.factories.BasketballFactory
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.smartShuffleList
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.basketball.GreatLakesConference
import com.appdev.jphil.basketballcoach.basketball.NortheasternAthleticAssociation
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
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
            var team = if (teamId == -1) {
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
            listOf(
                NortheasternAthleticAssociation(70),
                GreatLakesConference(90)
            ),
            resources.getStringArray(R.array.first_names).asList(),
            resources.getStringArray(R.array.last_names).asList()
        )
        val games = mutableListOf<Game>()
        var numberOfTeams = 0
        world.conferences.forEach {
            ConferenceDatabaseHelper.saveConference(it, database)
            games.addAll(it.generateSchedule(2018))
            numberOfTeams += it.teams.size
        }
        games.smartShuffleList(numberOfTeams)
        GameDatabaseHelper.saveOnlyGames(games, database)
        RecruitDatabaseHelper.saveRecruits(world.recruits, database)
    }

    override fun attachPresenter(presenter: RosterContract.Presenter) {
        this.presenter = presenter
    }
}