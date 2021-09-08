package com.appdev.jphil.basketballcoach.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.appdev.jphil.basketballcoach.database.coach.CoachDao
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDao
import com.appdev.jphil.basketballcoach.database.conference.ConferenceEntity
import com.appdev.jphil.basketballcoach.database.game.GameDao
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.game.GameEventEntity
import com.appdev.jphil.basketballcoach.database.player.GameStatsEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerDao
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerProgressionEntity
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDao
import com.appdev.jphil.basketballcoach.database.recruit.RecruitEntity
import com.appdev.jphil.basketballcoach.database.recruit.RecruitInterestEntity
import com.appdev.jphil.basketballcoach.database.relations.RelationalDao
import com.appdev.jphil.basketballcoach.database.team.TeamDao
import com.appdev.jphil.basketballcoach.database.team.TeamEntity

@Database(
    entities = [
        TeamEntity::class,
        PlayerEntity::class,
        CoachEntity::class,
        ConferenceEntity::class,
        GameEntity::class,
        GameStatsEntity::class,
        GameEventEntity::class,
        PlayerProgressionEntity::class,
        RecruitEntity::class,
        RecruitInterestEntity::class,
    ],
    version = 1
)
abstract class BasketballDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun teamDao(): TeamDao
    abstract fun conferenceDao(): ConferenceDao
    abstract fun gameDao(): GameDao
    abstract fun coachDao(): CoachDao
    abstract fun recruitDao(): RecruitDao
    abstract fun relationalDao(): RelationalDao
}
