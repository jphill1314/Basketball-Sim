package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.coaches.CoachType
import java.util.*

class TeamFactory(private val firstNames: List<String>, private val lastNames: List<String>) {

    fun generateTeam(teamId: Int, schoolName: String, mascot: String, teamAbbreviation: String, teamRating: Int, conferenceId: Int, isUser: Boolean): Team {
        // TODO: generate different kinds of teams: run and gun, press heavy, good offense, good defense, balances, etc
        return Team(
            teamId,
            schoolName,
            mascot,
            teamAbbreviation,
            generatePlayers(teamId, 15, teamRating),
            conferenceId,
            isUser,
            generateCoaches(teamId, teamRating),
            0
        )
    }

    private fun generatePlayers(teamId: Int, teamSize: Int, teamRating: Int): MutableList<Player> {
        val r = Random()
        val players = mutableListOf<Player>()

        for (position in 1..5) {
            players.add(
                PlayerFactory.generatePlayer(
                    firstNames[r.nextInt(firstNames.size)],
                    lastNames[r.nextInt(lastNames.size)],
                    position,
                    r.nextInt(4),
                    teamId,
                    teamRating,
                    position - 1
                )
            )
        }

        for (position in 1..5) {
            players.add(
                PlayerFactory.generatePlayer(
                    firstNames[r.nextInt(firstNames.size)],
                    lastNames[r.nextInt(lastNames.size)],
                    position,
                    r.nextInt(4),
                    teamId,
                    teamRating - r.nextInt(10),
                    position + 4
                )
            )
        }

        for (position in 1..(teamSize - 10)) {
            players.add(
                PlayerFactory.generatePlayer(
                    firstNames[r.nextInt(firstNames.size)],
                    lastNames[r.nextInt(lastNames.size)],
                    position,
                    r.nextInt(4),
                    teamId,
                    teamRating - r.nextInt(15),
                    position + 9
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