package com.appdev.jphil.basketballcoach.newseason

import android.content.res.Resources
import com.appdev.jphil.basketball.factories.BasketballFactory
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.schedule.NonConferenceScheduleGen
import com.appdev.jphil.basketball.schedule.smartShuffleList
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.basketball.AtlanticAthleticAssociation
import com.appdev.jphil.basketballcoach.basketball.CaliforniaConference
import com.appdev.jphil.basketballcoach.basketball.CanadianAthleticConference
import com.appdev.jphil.basketballcoach.basketball.DesertConference
import com.appdev.jphil.basketballcoach.basketball.GreatLakesConference
import com.appdev.jphil.basketballcoach.basketball.GulfCoastConference
import com.appdev.jphil.basketballcoach.basketball.MiddleAmericaConference
import com.appdev.jphil.basketballcoach.basketball.MountainAthleticAssociation
import com.appdev.jphil.basketballcoach.basketball.NortheasternAthleticAssociation
import com.appdev.jphil.basketballcoach.basketball.TobaccoConference
import com.appdev.jphil.basketballcoach.basketball.WesternConference
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object NewGameGenerator {

    const val NON_CON_GAMES = 10
    const val NUM_RECRUITS = 600

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
        }.join()
    }
}
