package com.appdev.jphil.basketballcoach.database.recruit

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.appdev.jphil.basketball.recruits.NewRecruitInterest

@Entity
data class RecruitInterestEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val recruitId: Int,
    val teamId: Int,
    val preferredPrestige: Int,
    val wantsClose: Boolean,
    val wantsFar: Boolean,
    val wantsImmediateStart: Boolean,
    val wantsToDevelop: Boolean,
    val wantsThrees: Boolean,
    val wantsPress: Boolean,
    val wantsAggressive: Boolean,
    val wantsToBeStar: Boolean,
    val prestigeInterest: Int?,
    val locationInterest: Int?,
    val playingTimeInterest: Int?,
    val playStyleInterest: Int?,
    val teamAbilityInterest: Int?,
    val recruitmentInterest: Int,
) {

    fun create(): NewRecruitInterest {
        return NewRecruitInterest(
            id,
            teamId,
            preferredPrestige,
            wantsClose,
            wantsFar,
            wantsImmediateStart,
            wantsToDevelop,
            wantsThrees,
            wantsPress,
            wantsAggressive,
            wantsToBeStar,
            prestigeInterest,
            locationInterest,
            playingTimeInterest,
            playStyleInterest,
            teamAbilityInterest,
            recruitmentInterest
        )
    }

    companion object {
        fun from(
            recruitId: Int,
            interest: NewRecruitInterest
        ): RecruitInterestEntity {
            return RecruitInterestEntity(
                interest.id,
                recruitId,
                interest.teamId,
                interest.preferredPrestige,
                interest.wantsClose,
                interest.wantsFar,
                interest.wantsImmediateStart,
                interest.wantsToDevelop,
                interest.wantsThrees,
                interest.wantsPress,
                interest.wantsAggressive,
                interest.wantsToBeStar,
                interest.prestigeInterest,
                interest.locationInterest,
                interest.playingTimeInterest,
                interest.playStyleInterest,
                interest.teamAbilityInterest,
                interest.recruitmentInterest
            )
        }
    }
}
