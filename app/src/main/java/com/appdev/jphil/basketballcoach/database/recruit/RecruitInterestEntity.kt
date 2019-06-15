package com.appdev.jphil.basketballcoach.database.recruit

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.appdev.jphil.basketball.recruits.RecruitInterest

@Entity
data class RecruitInterestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val recruitId: Int,
    val teamId: Int,
    val teamName: String,
    val interest: Int,
    val ratingRange: Int,
    val ratingOffset: Int,
    val isOfferedScholarship: Boolean,
    val lastInteractionGame: Int,
    val isScholarshipRevoked: Boolean
) {

    fun create(): RecruitInterest {
        return RecruitInterest(
            id,
            recruitId,
            teamId,
            teamName,
            interest,
            ratingRange,
            ratingOffset,
            isOfferedScholarship,
            lastInteractionGame,
            isScholarshipRevoked
        )
    }

    companion object {
        fun from(interest: RecruitInterest): RecruitInterestEntity {
            return RecruitInterestEntity(
                interest.id,
                interest.recruitId,
                interest.teamId,
                interest.teamName,
                interest.interest,
                interest.ratingRange,
                interest.ratingOffset,
                interest.isOfferedScholarship,
                interest.lastInteractionGame,
                interest.isScholarshipRevoked
            )
        }
    }
}