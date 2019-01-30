package com.appdev.jphil.basketballcoach.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [
    TeamEntity::class,
    PlayerEntity::class,
    ConferenceEntity::class,
    GameEntity::class],
    version = 1)
abstract class BasketballDatabase: RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun teamDao(): TeamDao
    abstract fun conferenceDao(): ConferenceDao
    abstract fun gameDao(): GameDao
}