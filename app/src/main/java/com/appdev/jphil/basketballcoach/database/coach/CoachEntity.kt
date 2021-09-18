package com.appdev.jphil.basketballcoach.database.coach

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.appdev.jphil.basketball.Pronouns
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.coaches.CoachType
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketballcoach.database.relations.RecruitRelations
import com.appdev.jphil.basketballcoach.database.typeconverters.IntListTypeConverter

@Entity
@TypeConverters(IntListTypeConverter::class)
data class CoachEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val teamId: Int,
    val type: CoachType,
    val firstName: String,
    val lastName: String,
    val pronouns: Pronouns,
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
    val intentionallyFoul: Boolean,
    val shouldHurry: Boolean,
    val shouldWasteTime: Boolean,
    val teachShooting: Int,
    val teachPostMoves: Int,
    val teachBallControl: Int,
    val teachPostDefense: Int,
    val teachPerimeterDefense: Int,
    val teachPositioning: Int,
    val teachRebounding: Int,
    val teachConditioning: Int,
    val recruitIds: List<Int>
) {

    fun createCoachWithRecruits(allRecruits: List<Recruit>): Coach {
        val recruits = allRecruits.filter {
            !it.isCommitted && recruitIds.contains(it.id)
        }

        return Coach(
            id,
            teamId,
            type,
            firstName,
            lastName,
            pronouns,
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
            intentionallyFoul,
            shouldHurry,
            shouldWasteTime,
            teachShooting,
            teachPostMoves,
            teachBallControl,
            teachPostDefense,
            teachPerimeterDefense,
            teachPositioning,
            teachRebounding,
            teachConditioning,
        ).apply {
            recruitingAssignments.addAll(recruits)
        }
    }

    fun createCoach(allRecruits: List<RecruitRelations>): Coach {
        val recruits = allRecruits.filter { recruitIds.contains(it.recruitEntity.id) }.map {
            it.recruitEntity.createRecruit(it.recruitInterestEntities)
        }

        return Coach(
            id,
            teamId,
            type,
            firstName,
            lastName,
            pronouns,
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
            intentionallyFoul,
            shouldHurry,
            shouldWasteTime,
            teachShooting,
            teachPostMoves,
            teachBallControl,
            teachPostDefense,
            teachPerimeterDefense,
            teachPositioning,
            teachRebounding,
            teachConditioning,
        ).apply {
            recruitingAssignments.addAll(recruits)
        }
    }

    companion object {
        fun from(coach: Coach): CoachEntity {
            return CoachEntity(
                coach.id,
                coach.teamId,
                coach.type,
                coach.firstName,
                coach.lastName,
                coach.pronouns,
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
                coach.intentionallyFoul,
                coach.shouldHurry,
                coach.shouldWasteTime,
                coach.teachShooting,
                coach.teachPostMoves,
                coach.teachBallControl,
                coach.teachPostDefense,
                coach.teachPerimeterDefense,
                coach.teachPositioning,
                coach.teachRebounding,
                coach.teachConditioning,
                coach.recruitingAssignments.map { it.id }
            )
        }
    }
}
