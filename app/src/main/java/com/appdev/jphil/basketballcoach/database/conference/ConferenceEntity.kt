package com.appdev.jphil.basketballcoach.database.conference

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ConferenceEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val tournamentIsFinished: Boolean,
    val championId: Int
)
