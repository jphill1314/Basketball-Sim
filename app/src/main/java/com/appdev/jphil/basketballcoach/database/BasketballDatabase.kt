package com.appdev.jphil.basketballcoach.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [TeamEntity::class, PlayerEntity::class], version = 1)
abstract class BasketballDatabase: RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun teamDao(): TeamDao
}