package com.appdev.jphil.basketballcoach.database.game

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class GameEventEntity(
    @PrimaryKey
    val id: Int,
    val gameId: Int,
    val timeRemaining: Int,
    val shotClock: Int,
    var event: String,
    val homeTeam: String,
    val awayTeam: String,
    val homeScore: Int,
    val awayScore: Int,
    val homeTeamHasBall: Boolean
)