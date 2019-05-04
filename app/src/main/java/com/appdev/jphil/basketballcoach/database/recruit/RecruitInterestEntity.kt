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
    val teamName: String,
    val interest: Int,
    val isScouted: Boolean,
    val isContacted: Boolean,
    val isOfferedScholarship: Boolean,
    val isOfficialVisitDone: Boolean,
    val lastInteractionGame: Int,
    val timesScouted: Int,
    val timesContacted: Int,
    val isScholarshipRevoked: Boolean
) {

    fun create(): RecruitInterest {
        return RecruitInterest(
            id,
            recruitId,
            teamId,
            teamName,
            interest,
            isScouted,
            isContacted,
            isOfferedScholarship,
            isOfficialVisitDone,
            lastInteractionGame,
            timesScouted,
            timesContacted,
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
                interest.isScouted,
                interest.isContacted,
                interest.isOfferedScholarship,
                interest.isOfficialVisitDone,
                interest.lastInteractionGame,
                interest.timesScouted,
                interest.timesContacted,
                interest.isScholarshipRevoked
            )
        }
    }
}