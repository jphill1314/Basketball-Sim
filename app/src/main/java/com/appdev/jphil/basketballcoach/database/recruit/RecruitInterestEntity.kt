package com.appdev.jphil.basketballcoach.database.recruit

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.appdev.jphil.basketball.recruits.RecruitInterest

@Entity
data class RecruitInterestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val recruitId: Int,
    val teamId: Int,
    val interest: Int,
    val isScouted: Boolean,
    val isContacted: Boolean,
    val isOfferedScholarship: Boolean,
    val isOfficialVisitDone: Boolean,
    val lastInteractionGame: Int
) {

    fun create(): RecruitInterest {
        return RecruitInterest(
            id,
            recruitId,
            teamId,
            interest,
            isScouted,
            isContacted,
            isOfferedScholarship,
            isOfficialVisitDone,
            lastInteractionGame
        )
    }

    companion object {
        fun from(interest: RecruitInterest): RecruitInterestEntity {
            return RecruitInterestEntity(
                interest.id,
                interest.recruitId,
                interest.teamId,
                interest.interest,
                interest.isScouted,
                interest.isContacted,
                interest.isOfferedScholarship,
                interest.isOfficialVisitDone,
                interest.lastInteractionGame
            )
        }
    }
}