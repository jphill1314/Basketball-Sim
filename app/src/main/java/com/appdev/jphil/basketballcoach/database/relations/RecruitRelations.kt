package com.appdev.jphil.basketballcoach.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.appdev.jphil.basketballcoach.database.recruit.RecruitEntity
import com.appdev.jphil.basketballcoach.database.recruit.RecruitInterestEntity

class RecruitRelations(
    @Embedded val recruitEntity: RecruitEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "recruitId"
    )
    val recruitInterestEntities: List<RecruitInterestEntity>
)
