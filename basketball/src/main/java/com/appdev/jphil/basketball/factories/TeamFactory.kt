package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketball.Team
import java.util.*

class TeamFactory(private val firstNames: List<String>, private val lastNames: List<String>) {

    fun generateTeam(teamId: Int, teamName: String, teamAbbreviation: String, teamRating: Int, conferenceId: Int, isUser: Boolean): Team {
        // TODO: generate different kinds of teams: run and gun, press heavy, good offense, good defense, balances, etc
        return Team(
            teamId,
            teamName,
            teamAbbreviation,
            50,
            50,
            0,
            0,
            50,
            70,
            generatePlayers(teamId, 15, teamRating),
            conferenceId,
            isUser
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
                    teamId,
                    teamRating - r.nextInt(15),
                    i + 9
                )
            )
        }

        return players
    }
}