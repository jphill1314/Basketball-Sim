package com.appdev.jphil.basketballcoach.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.appdev.jphil.basketballcoach.database.conference.ConferenceEntity
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.database.team.TeamEntity

data class ConferenceTournamentRelations(
    @Embedded val conferenceEntity: ConferenceEntity,
    @Relation(
        entity = TeamEntity::class,
        parentColumn = "id",
        entityColumn = "conferenceId"
    )
    val teamEntities: List<TeamRelations>,
    @Relation(
        parentColumn = "id",
        entityColumn = "tournamentId"
    )
    val tournamentGameEntities: List<GameEntity>
)
