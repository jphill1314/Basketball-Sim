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
    val homeTeamHasBall: Boolean
)