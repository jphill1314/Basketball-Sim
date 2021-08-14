package com.appdev.jphil.basketballcoach.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.database.coach.ScoutingAssignmentEntity

data class CoachRelations(
    @Embedded val coachRelations: CoachEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "coachId"
    )
    val assignmentEntity: ScoutingAssignmentEntity
)