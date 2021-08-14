package com.appdev.jphil.basketballcoach.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity
import com.appdev.jphil.basketballcoach.database.team.TeamEntity

data class TeamRelations(
    @Embedded val teamEntity: TeamEntity,

    @Relation(
        entity = PlayerEntity::class,
        parentColumn = "teamId",
        entityColumn = "teamId"
    )
    val playerEntities: List<PlayerRelations>,

    @Relation(
        entity = CoachEntity::class,
        parentColumn = "teamId",
        entityColumn = "teamId"
    )
    val coachEntities: List<CoachRelations>
)
