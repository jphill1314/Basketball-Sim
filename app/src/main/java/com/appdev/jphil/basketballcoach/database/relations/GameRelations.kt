package com.appdev.jphil.basketballcoach.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.team.TeamEntity

data class GameRelations(
    @Embedded val gameEntity: GameEntity,

    @Relation(
        entity = TeamEntity::class,
        parentColumn = "homeTeamId",
        entityColumn = "teamId"
    )
    val homeTeam: TeamRelations,

    @Relation(
        entity = TeamEntity::class,
        parentColumn = "awayTeamId",
        entityColumn = "teamId"
    )
    val awayTeam: TeamRelations
)