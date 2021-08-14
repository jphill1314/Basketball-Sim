package com.appdev.jphil.basketballcoach.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerProgressionEntity

data class PlayerRelations(
    @Embedded val playerEntity: PlayerEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "playerId"
    )
    val progressions: List<PlayerProgressionEntity>
)