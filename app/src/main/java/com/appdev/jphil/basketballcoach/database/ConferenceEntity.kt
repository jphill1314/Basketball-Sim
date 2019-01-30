package com.appdev.jphil.basketballcoach.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class ConferenceEntity(
    @PrimaryKey
    val id: Int,
    val name: String
)