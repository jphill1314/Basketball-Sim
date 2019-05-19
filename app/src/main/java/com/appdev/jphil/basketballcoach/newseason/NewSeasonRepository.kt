package com.appdev.jphil.basketballcoach.newseason

import android.content.res.Resources
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.factories.PlayerFactory
import com.appdev.jphil.basketball.factories.RecruitFactory
import com.appdev.jphil.basketball.players.PracticeType
import com.appdev.jphil.basketball.recruits.Recruit
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
import javax.inject.Inject
import kotlin.random.Random

class NewSeasonRepository @Inject constructor(
    private val database: BasketballDatabase,
    resources: Resources
) {

    private val firstNames = resources.getStringArray(R.array.first_names).asList()
    private val lastNames = resources.getStringArray(R.array.last_names).asList()

    fun startNewSeason(callback: () -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            GameDatabaseHelper.deleteAllGames(database)
            val conferences = ConferenceDatabaseHelper.loadAllConferences(database)
            val recruits = RecruitDatabaseHelper.loadAllRecruits(database)
            conferences.forEach { conference ->
                conference.teams.forEach {
                    startNewSeasonForTeam(it, recruits)
                }
                GameDatabaseHelper.saveOnlyGames(conference.generateSchedule(2018), database)
                ConferenceDatabaseHelper.saveConference(conference, database)
            }
            RecruitDatabaseHelper.deleteAllRecruits(database)

            val newRecruits = RecruitFactory.generateRecruits(firstNames, lastNames, 100)
            RecruitDatabaseHelper.saveRecruits(newRecruits, database)

            withContext(Dispatchers.Main) { callback() }
        }
    }

    private fun startNewSeasonForTeam(team: Team, recruits: List<Recruit>) {
        // Make each player a year older
        team.players.forEach { player ->
            player.year++

            if (player.year > 3) {
                PlayerDatabaseHelper.deletePlayer(player, database)
            }
        }

        // Remove players who graduated
        team.returningPlayers(team.players.filter { it.year < 4 })

        // Extra improvement for returning players
        for (i in 1..(PRACTICES / 2)) {
            team.roster.forEach { it.runPractice(PracticeType.NO_FOCUS, team.coaches) }
        }

        // Add commits to team
        recruits.filter { it.isCommitted && it.teamCommittedTo == team.teamId }.forEach { commit ->
            team.addNewPlayer(commit.generatePlayer(team.teamId, team.roster.size))
        }


        for (position in 1..5) {
            // Fill empty spots in roster
            while (team.players.filter { it.position == position }.size < 2) {
                team.addNewPlayer(PlayerFactory.generatePlayer(
                    firstNames[Random.nextInt(firstNames.size)],
                    lastNames[Random.nextInt(lastNames.size)],
                    position,
                    0,
                    team.teamId,
                    Random.nextInt(WALK_ON_VARIATION) + WALK_ON_MIN,
                    team.players.size
                ))
            }

            // Sort roster so that best players start
            val players = team.players.filter { it.position == position }.sortedByDescending { it.getOverallRating() }
            for (index in players.indices) {
                players[index].apply {
                    rosterIndex = index * 5 + position - 1
                    courtIndex = rosterIndex
                    subPosition = rosterIndex
                }
            }
        }

        // Improve all players on team
        for (i in 1..(PRACTICES / 2)) {
            team.roster.forEach { it.runPractice(PracticeType.NO_FOCUS, team.coaches) }
        }

        team.players.sortBy { it.rosterIndex }
    }

    companion object {
        private const val PRACTICES = 50
        private const val WALK_ON_VARIATION = 15
        private const val WALK_ON_MIN = 20
    }
}