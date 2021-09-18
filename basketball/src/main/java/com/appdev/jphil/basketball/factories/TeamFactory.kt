package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.Pronouns
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.coaches.CoachType
import com.appdev.jphil.basketball.location.Location
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.teams.TeamColor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
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
        location: Location,
        firstNames: List<String>,
        lastNames: List<String>,
        headCoachFirstName: String? = null,
        headCoachLastName: String? = null,
        headCoachPronouns: Pronouns = Pronouns.HE
    ): Team {
        // TODO: generate different kinds of teams: run and gun, press heavy, good offense, good defense, balances, etc
        val players = generatePlayers(teamId, 15, teamRating, firstNames, lastNames)
        val newRating = players.map { it.getOverallRating() }.average().roundToInt()
        val prestige = max(0, min(100, newRating + Random.nextInt(20) - 10))

        return Team(
            teamId,
            schoolName,
            mascot,
            teamAbbreviation,
            color,
            players,
            conferenceId,
            isUser,
            generateCoaches(
                teamId,
                teamRating,
                firstNames,
                lastNames,
                headCoachFirstName,
                headCoachLastName,
                headCoachPronouns
            ),
            location,
            prestige,
            gamesPlayed = 0,
            postSeasonTournamentId = -1,
            postSeasonTournamentSeed = -1
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

    private fun generateCoaches(
        teamId: Int,
        rating: Int,
        firstNames: List<String>,
        lastNames: List<String>,
        headCoachFirstName: String?,
        headCoachLastName: String?,
        headCoachPronouns: Pronouns
    ): MutableList<Coach> {
        val coaches = mutableListOf(
            if (headCoachFirstName == null || headCoachLastName == null) {
                generateCoach(
                    teamId,
                    CoachType.HEAD_COACH,
                    rating,
                    firstNames,
                    lastNames
                )
            } else {
                CoachFactory.generateCoach(
                    teamId,
                    CoachType.HEAD_COACH,
                    headCoachFirstName,
                    headCoachLastName,
                    rating,
                    headCoachPronouns
                )
            }
        )
        for (i in 1..3) {
            coaches.add(
                generateCoach(
                    teamId,
                    CoachType.ASSISTANT_COACH,
                    rating - Random.nextInt(5 * i),
                    firstNames,
                    lastNames
                )
            )
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
