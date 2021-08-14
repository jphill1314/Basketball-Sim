package com.appdev.jphil.basketballcoach.database.coach

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.appdev.jphil.basketball.coaches.ScoutingAssignment
import com.appdev.jphil.basketballcoach.database.typeconverters.IntListTypeConverter

@Entity
@TypeConverters(IntListTypeConverter::class)
class ScoutingAssignmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val coachId: Int,
    val positions: MutableList<Int>,
    val minRating: Int,
    val maxRating: Int,
    val minPotential: Int,
    val maxPotential: Int
) {

    fun create(): ScoutingAssignment {
        return ScoutingAssignment(id, positions, minRating, maxRating, minPotential, maxPotential)
    }

    companion object {
        fun from(coachId: Int, assignment: ScoutingAssignment): ScoutingAssignmentEntity {
            return ScoutingAssignmentEntity(
                assignment.id,
                coachId,
                assignment.positions,
                assignment.minRating,
                assignment.maxRating,
                assignment.minPotential,
                assignment.maxPotential
            )
        }
    }
}
