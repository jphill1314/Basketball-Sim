package com.appdev.jphil.basketballcoach.database.recruit

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.appdev.jphil.basketball.players.PlayerType
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.recruits.RecruitInterest

@Entity
data class RecruitEntity(
    @PrimaryKey val id: Int,
    val firstName: String,
    val lastName: String,
    val position: Int,
    val playerType: Int,
    val rating: Int,
    val isCommitted: Boolean
) {

    fun createRecruit(teamInterest: List<RecruitInterestEntity>): Recruit {
        val interest = mutableListOf<RecruitInterest>()
        teamInterest.forEach { interest.add(it.create()) }

        val type = when (playerType) {
            PlayerType.SHOOTER.type -> PlayerType.SHOOTER
            PlayerType.DISTRIBUTOR.type -> PlayerType.DISTRIBUTOR
            PlayerType.REBOUNDER.type -> PlayerType.REBOUNDER
            PlayerType.DEFENDER.type -> PlayerType.DEFENDER
            else -> PlayerType.BALANCED
        }

        return Recruit(
            id,
            firstName,
            lastName,
            position,
            type,
            rating,
            isCommitted,
            interest
        )
    }

    companion object {
        fun from(recruit: Recruit): RecruitEntity {
            return RecruitEntity(
                recruit.id,
                recruit.firstName,
                recruit.lastName,
                recruit.position,
                recruit.playerType.type,
                recruit.rating,
                recruit.isCommitted
            )
        }
    }
}