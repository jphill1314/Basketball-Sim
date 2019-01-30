package com.appdev.jphil.basketballcoach.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class GameEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val homeTeamId: Int,
    val awayTeamId: Int,
    val isNeutralCourt: Boolean
)