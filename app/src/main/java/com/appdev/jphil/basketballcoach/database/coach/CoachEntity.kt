package com.appdev.jphil.basketballcoach.database.coach

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.coaches.CoachType
import com.appdev.jphil.basketball.coaches.ScoutingAssignment

@Entity
data class CoachEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val teamId: Int,
    val type: Int,
    val firstName: String,
    val lastName: String,
    val recruiting: Int,
    val offenseFavorsThrees: Int,
    val pace: Int,
    val aggression: Int,
    val defenseFavorsThrees: Int,
    val pressFrequency: Int,
    val pressAggression: Int,
    val offenseFavorsThreesGame: Int,
    val paceGame: Int,
    val aggressionGame: Int,
    val defenseFavorsThreesGame: Int,
    val pressFrequencyGame: Int,
    val pressAggressionGame: Int,
    val teachShooting: Int,
    val teachPostMoves: Int,
    val teachBallControl: Int,
    val teachPostDefense: Int,
    val teachPerimeterDefense: Int,
    val teachPositioning: Int,
    val teachRebounding: Int,
    val teachConditioning: Int
) {

    fun createCoach(scoutingAssignmentEntity: ScoutingAssignmentEntity?): Coach {
        val coachType = when (type) {
            CoachType.HEAD_COACH.type -> CoachType.HEAD_COACH
            else -> CoachType.ASSISTANT_COACH
        }

        return Coach(
            id,
            teamId,
            coachType,
            firstName,
            lastName,
            recruiting,
            offenseFavorsThrees,
            pace,
            aggression,
            defenseFavorsThrees,
            pressFrequency,
            pressAggression,
            offenseFavorsThreesGame,
            paceGame,
            aggressionGame,
            defenseFavorsThreesGame,
            pressFrequencyGame,
            pressAggressionGame,
            teachShooting,
            teachPostMoves,
            teachBallControl,
            teachPostDefense,
            teachPerimeterDefense,
            teachPositioning,
            teachRebounding,
            teachConditioning,
            scoutingAssignmentEntity?.create() ?: ScoutingAssignment()
        )
    }

    companion object {
        fun from(coach: Coach): CoachEntity {
            return CoachEntity(
                coach.id,
                coach.teamId,
                coach.type.type,
                coach.firstName,
                coach.lastName,
                coach.recruiting,
                coach.offenseFavorsThrees,
                coach.pace,
                coach.aggression,
                coach.defenseFavorsThrees,
                coach.pressFrequency,
                coach.pressAggression,
                coach.offenseFavorsThreesGame,
                coach.paceGame,
                coach.aggressionGame,
                coach.defenseFavorsThreesGame,
                coach.pressFrequencyGame,
                coach.pressAggressionGame,
                coach.teachShooting,
                coach.teachPostMoves,
                coach.teachBallControl,
                coach.teachPostDefense,
                coach.teachPerimeterDefense,
                coach.teachPositioning,
                coach.teachRebounding,
                coach.teachConditioning
            )
        }
    }
}