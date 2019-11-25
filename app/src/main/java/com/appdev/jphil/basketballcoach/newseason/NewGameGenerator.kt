package com.appdev.jphil.basketballcoach.newseason

import android.content.res.Resources
import android.util.Log
import com.appdev.jphil.basketball.factories.BasketballFactory
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.schedule.NonConferenceScheduleGen
import com.appdev.jphil.basketball.schedule.smartShuffleList
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.basketball.*
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object NewGameGenerator {

    suspend fun generateNewGame(resources: Resources, database: BasketballDatabase) {
        GlobalScope.launch {
            val world = BasketballFactory.setupWholeBasketballWorld(
                listOf(
                    NortheasternAthleticAssociation(70),
                    GreatLakesConference(75),
                    GulfCoastConference(70),
                    TobaccoConference(55),
                    AtlanticAthleticAssociation(75),
                    MountainAthleticAssociation(60),
                    MiddleAmericaConference(55),
                    CaliforniaConference(65),
                    DesertConference(60),
                    WesternConference(55),
                    CanadianAthleticConference(50)
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
//            val nonConGames = NonConferenceScheduleGen.generateNonConferenceSchedule(world.conferences, 10, 2018)
//            nonConGames.smartShuffleList(numberOfTeams)
//            GameDatabaseHelper.saveOnlyGames(nonConGames, database)
//            Log.d("NonCon", "NonConGames: ${nonConGames.size}")

            games.smartShuffleList(numberOfTeams)
            GameDatabaseHelper.saveOnlyGames(games, database)
            RecruitDatabaseHelper.saveRecruits(world.recruits, database)
        }.join()
    }

}