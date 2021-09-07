package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.coaches.CoachType
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.teams.TeamColor
import kotlin.random.Random

object TeamFactory {

    private const val STARTER = 0
    private const val BENCH = 1
    private const val RESERVE = 2
    private const val RATING_VARIABILITY = 10

    fun generateTeam(
        teamId: Int,
        schoolName: String,
        mascot: String,
        color: TeamColor,
        teamAbbreviation: String,
        teamRating: Int,
        conferenceId: Int,
        isUser: Boolean,
        firstNames: List<String>,
        lastNames: List<String>
    ): Team {
        // TODO: generate different kinds of teams: run and gun, press heavy, good offense, good defense, balances, etc
        return Team(
            teamId,
            schoolName,
            mascot,
            teamAbbreviation,
            color,
            generatePlayers(teamId, 15, teamRating, firstNames, lastNames),
            conferenceId,
            isUser,
            generateCoaches(teamId, teamRating, firstNames, lastNames),
            0,
            -1,
            -1,
            mutableListOf()
        )
    }

    private fun generatePlayers(
        teamId: Int,
        teamSize: Int,
        teamRating: Int,
        firstNames: List<String>,
        lastNames: List<String>
    ): MutableList<Player> {
        val players = mutableListOf<Player>()

        for (position in 1..5) {
            players.add(
                PlayerFactory.generatePlayer(
                    firstNames[Random.nextInt(firstNames.size)],
                    lastNames[Random.nextInt(lastNames.size)],
                    position,
                    Random.nextInt(4),
                    teamId,
                    generatePlayerRating(teamRating, STARTER),
                    position - 1
                )
            )
        }

        for (position in 1..5) {
            players.add(
                PlayerFactory.generatePlayer(
                    firstNames[Random.nextInt(firstNames.size)],
                    lastNames[Random.nextInt(lastNames.size)],
                    position,
                    Random.nextInt(4),
                    teamId,
                    generatePlayerRating(teamRating, BENCH),
                    position + 4
                )
            )
        }

        for (position in 1..(teamSize - 10)) {
            players.add(
                PlayerFactory.generatePlayer(
                    firstNames[Random.nextInt(firstNames.size)],
                    lastNames[Random.nextInt(lastNames.size)],
                    position,
                    Random.nextInt(4),
                    teamId,
                    generatePlayerRating(teamRating, RESERVE),
                    position + 9
                )
            )
        }

        return players
    }

    private fun generateCoaches(teamId: Int, rating: Int, firstNames: List<String>, lastNames: List<String>): MutableList<Coach> {
        val coaches = mutableListOf(generateCoach(teamId, CoachType.HEAD_COACH, rating, firstNames, lastNames))
        for (i in 1..3) {
            coaches.add(generateCoach(teamId, CoachType.ASSISTANT_COACH, rating - Random.nextInt(5 * i), firstNames, lastNames))
        }
        return coaches
    }

    private fun generateCoach(
        teamId: Int,
        type: CoachType,
        rating: Int,
        firstNames: List<String>,
        lastNames: List<String>
    ): Coach {
        return CoachFactory.generateCoach(
            teamId,
            type,
            firstNames[Random.nextInt(firstNames.size)],
            lastNames[Random.nextInt(lastNames.size)],
            rating
        )
    }

    private fun generatePlayerRating(teamRating: Int, type: Int): Int {
        return when (type) {
            STARTER -> teamRating + Random.nextInt(3 * RATING_VARIABILITY) - RATING_VARIABILITY
            BENCH -> teamRating + Random.nextInt(2 * RATING_VARIABILITY) - 2 * RATING_VARIABILITY
            else -> teamRating + Random.nextInt(RATING_VARIABILITY) - 2 * RATING_VARIABILITY
        }
    }
}
