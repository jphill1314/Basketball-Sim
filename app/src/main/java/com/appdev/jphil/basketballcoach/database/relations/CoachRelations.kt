package com.appdev.jphil.basketballcoach.database.relations

import androidx.room.Embedded
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity

data class CoachRelations(
    @Embedded val coachRelations: CoachEntity,
)
