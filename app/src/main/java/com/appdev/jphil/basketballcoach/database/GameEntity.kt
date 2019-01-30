package com.appdev.jphil.basketballcoach.database

import android.arch.persistence.room.Entity

@Entity
data class GameEntity(
    val id: Int,
    val homeTeamId: Int,
    val awayTeamId: Int,
    val isNeutralCourt: Boolean
)