package com.appdev.jphil.basketballcoach.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDao
import com.appdev.jphil.basketballcoach.database.conference.ConferenceEntity
import com.appdev.jphil.basketballcoach.database.game.GameDao
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerDao
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity
import com.appdev.jphil.basketballcoach.database.team.TeamDao
import com.appdev.jphil.basketballcoach.database.team.TeamEntity

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