package com.appdev.jphil.basketballcoach.newgame

import android.content.res.Resources
import com.appdev.jphil.basketball.factories.BasketballFactory
import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.schedule.NonConferenceScheduleGen
import com.appdev.jphil.basketball.schedule.smartShuffleList
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.coach.CoachDao
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDao
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.player.PlayerDao
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDao
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.database.team.TeamDao
import javax.inject.Inject

class NewGameRepository @Inject constructor(
    private val resources: Resources,
    private val database: BasketballDatabase,
    private val teamDao: TeamDao,
    private val coachDao: CoachDao,
    private val conferenceDao: ConferenceDao,
    private val playerDao: PlayerDao,
    private val recruitDao: RecruitDao
) {

    companion object {
        const val NON_CON_GAMES = 10
        const val NUM_RECRUITS = 600
    }

    suspend fun getUserTeamEntity() = teamDao.getTeamIsUser()

    suspend fun deleteExistingGame() {
        recruitDao.deleteAllRecruitInterests()
        recruitDao.deleteAllRecruits()

        playerDao.deleteAllPlayerProgress()
        playerDao.deleteAllGameStats()
        playerDao.deleteAllPlayers()

        coachDao.deleteAllCoaches()

        teamDao.deleteAllTeams()

        conferenceDao.deleteAllConferences()
    }

    suspend fun generateNewGame(conferences: List<ConferenceGeneratorDataModel>) {
        val world = BasketballFactory.setupWholeBasketballWorld(
            conferences,
            resources.getStringArray(R.array.first_names).asList(),
            resources.getStringArray(R.array.last_names).asList(),
            NUM_RECRUITS
        )
        val games = mutableListOf<Game>()
        var numberOfTeams = 0
        world.conferences.forEach {
            ConferenceDatabaseHelper.saveConference(it, database)
            games.addAll(it.generateSchedule(2018))
            numberOfTeams += it.teams.size
        }
        val nonConGames = NonConferenceScheduleGen.generateNonConferenceSchedule(world.conferences, NON_CON_GAMES, 2018)
        nonConGames.smartShuffleList(numberOfTeams)
        GameDatabaseHelper.saveOnlyGames(nonConGames, database)

        games.smartShuffleList(numberOfTeams)
        GameDatabaseHelper.saveOnlyGames(games, database)
        RecruitDatabaseHelper.saveRecruits(world.recruits, database)
    }
}
