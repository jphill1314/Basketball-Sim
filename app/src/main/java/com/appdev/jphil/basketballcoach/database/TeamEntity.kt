package com.appdev.jphil.basketballcoach.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.util.Log
import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketball.Team

@Entity
data class TeamEntity(
    @PrimaryKey
    val teamId: Int,
    val name: String,
    val offenseFavorsThrees: Int,
    val defenseFavorsThrees: Int,
    val pressFrequency : Int,
    val pressAggression: Int,
    val aggression: Int,
    val pace: Int
) {
    constructor(team: Team): this(
        team.teamId,
        team.name,
        team.offenseFavorsThrees,
        team.defenseFavorsThrees,
        team.pressFrequency,
        team.pressAggression,
        team.aggression,
        team.pace
    )

    fun createTeam(players: List<PlayerEntity>): Team {
        val teamPlayers = mutableListOf<Player>()
        players.forEach { player -> teamPlayers.add(player.createPlayer()) }
        return Team(
            teamId,
            name,
            offenseFavorsThrees,
            defenseFavorsThrees,
            pressFrequency,
            pressAggression,
            aggression,
            pace,
            teamPlayers
        )
    }
}