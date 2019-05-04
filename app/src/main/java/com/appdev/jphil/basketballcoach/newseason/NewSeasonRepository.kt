package com.appdev.jphil.basketballcoach.newseason

import android.content.res.Resources
import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketball.factories.PlayerFactory
import com.appdev.jphil.basketball.factories.RecruitFactory
import com.appdev.jphil.basketball.players.PracticeType
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.player.PlayerDatabaseHelper
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class NewSeasonRepository @Inject constructor(
    private val database: BasketballDatabase,
    resources: Resources
) {

    private val r = Random()
    private val firstNames = resources.getStringArray(R.array.first_names).asList()
    private val lastNames = resources.getStringArray(R.array.last_names).asList()

    fun startNewSeason(callback: () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            GameDatabaseHelper.deleteAllGames(database)
            val conferences = ConferenceDatabaseHelper.loadAllConferences(database)
            conferences.forEach { conference ->
                conference.teams.forEach {
                    startNewSeasonForTeam(it)
                }
                GameDatabaseHelper.saveOnlyGames(conference.generateSchedule(2018), database)
                ConferenceDatabaseHelper.saveConference(conference, database)
            }
            RecruitDatabaseHelper.deleteAllRecruits(database)
            RecruitDatabaseHelper.saveRecruits(
                RecruitFactory.generateRecruits(firstNames, lastNames, 100),
                database
            )

            withContext(Dispatchers.Main) { callback() }
        }
    }

    private fun startNewSeasonForTeam(team: Team) {
        team.players.forEach { player ->
            player.year++

            if (player.year > 3) {
                PlayerDatabaseHelper.deletePlayer(player, database)
            }
        }

        team.returningPlayers(team.players.filter { it.year < 4 })

        for (i in 1..(PRACTICES / 2)) {
            team.roster.forEach { it.runPractice(PracticeType.NO_FOCUS, team.coaches) }
        }

        for (position in 1..5) {
            while (team.players.filter { it.position == position }.size < 3) {
                team.addNewPlayer(PlayerFactory.generatePlayer(
                    firstNames[r.nextInt(firstNames.size)],
                    lastNames[r.nextInt(lastNames.size)],
                    position,
                    0,
                    team.teamId,
                    team.teamRating + r.nextInt(20) - 10,
                    team.players.size
                ))
            }

            val players = team.players.filter { it.position == position }.sortedByDescending { it.getOverallRating() }
            for (index in players.indices) {
                players[index].apply {
                    rosterIndex = index * 5 + position - 1
                    courtIndex = rosterIndex
                    subPosition = rosterIndex
                }
            }
        }

        for (i in 1..(PRACTICES / 2)) {
            team.roster.forEach { it.runPractice(PracticeType.NO_FOCUS, team.coaches) }
        }

        team.players.sortBy { it.rosterIndex }
    }

    companion object {
        private const val PRACTICES = 50
    }
}