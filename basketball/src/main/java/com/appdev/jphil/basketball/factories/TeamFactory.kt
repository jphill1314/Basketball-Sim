package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketball.coaches.CoachType
import java.util.*

class TeamFactory(private val firstNames: List<String>, private val lastNames: List<String>) {

    fun generateTeam(teamId: Int, teamName: String, teamAbbreviation: String, teamRating: Int, conferenceId: Int, isUser: Boolean): Team {
        // TODO: generate different kinds of teams: run and gun, press heavy, good offense, good defense, balances, etc
        return Team(
            teamId,
            teamName,
            teamAbbreviation,
            generatePlayers(teamId, 15, teamRating),
            conferenceId,
            isUser,
            generateCoaches(teamId, teamRating)
        )
    }

    private fun generatePlayers(teamId: Int, teamSize: Int, teamRating: Int): MutableList<Player> {
        val r = Random()
        val players = mutableListOf<Player>()

        for (i in 1..5) {
            players.add(
                PlayerFactory.generateBalancedPlayer(
                    firstNames[r.nextInt(firstNames.size)],
                    lastNames[r.nextInt(lastNames.size)],
                    i,
                    r.nextInt(4),
                    teamId,
                    teamRating,
                    i - 1
                )
            )
        }

        for (i in 1..5) {
            players.add(
                PlayerFactory.generateBalancedPlayer(
                    firstNames[r.nextInt(firstNames.size)],
                    lastNames[r.nextInt(lastNames.size)],
                    i,
                    r.nextInt(4),
                    teamId,
                    teamRating - r.nextInt(10),
                    i + 4
                )
            )
        }

        for (i in 1..(teamSize - 10)) {
            players.add(
                PlayerFactory.generateBalancedPlayer(
                    firstNames[r.nextInt(firstNames.size)],
                    lastNames[r.nextInt(lastNames.size)],
                    i,
                    r.nextInt(4),
                    teamId,
                    teamRating - r.nextInt(15),
                    i + 9
                )
            )
        }

        return players
    }

    private fun generateCoaches(teamId: Int, rating: Int): MutableList<Coach> {
        val coaches = mutableListOf(generateCoach(teamId, CoachType.HEAD_COACH, rating))
        val r = Random()
        for (i in 1..3) {
            coaches.add(generateCoach(teamId, CoachType.ASSISTANT_COACH, rating - r.nextInt(5 * i)))
        }
        return coaches
    }

    private fun generateCoach(teamId: Int, type: CoachType, rating: Int): Coach {
        val r = Random()
        return CoachFactory.generateCoach(
            teamId,
            type,
            firstNames[r.nextInt(firstNames.size)],
            lastNames[r.nextInt(lastNames.size)],
            rating
        )
    }
}