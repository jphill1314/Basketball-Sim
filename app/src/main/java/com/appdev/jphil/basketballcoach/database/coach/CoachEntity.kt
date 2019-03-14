package com.appdev.jphil.basketballcoach.database.coach

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.appdev.jphil.basketball.Coach

@Entity
data class CoachEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val teamId: Int,
    val firstName: String,
    val lastName: String,
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
    val pressAggressionGame: Int
) {

    fun createCoach(): Coach {
        return Coach(
            id,
            teamId,
            firstName,
            lastName,
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
            pressAggressionGame
        )
    }

    companion object {
        fun from(coach: Coach): CoachEntity {
            return CoachEntity(
                coach.id,
                coach.teamId,
                coach.firstName,
                coach.lastName,
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
                coach.pressAggressionGame
            )
        }
    }
}