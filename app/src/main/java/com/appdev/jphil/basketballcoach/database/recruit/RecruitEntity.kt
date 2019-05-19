package com.appdev.jphil.basketballcoach.database.recruit

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.appdev.jphil.basketball.players.PlayerType
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.recruits.RecruitDesire
import com.appdev.jphil.basketball.recruits.RecruitInterest
import com.appdev.jphil.basketballcoach.database.typeconverters.IntListTypeConverter

@Entity
@TypeConverters(IntListTypeConverter::class)
data class RecruitEntity(
    @PrimaryKey val id: Int,
    val firstName: String,
    val lastName: String,
    val position: Int,
    val playerType: Int,
    val desires: List<Int>,
    val rating: Int,
    val potential: Int,
    val teamCommittedTo: Int,
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

        val recruitDesires = mutableListOf<RecruitDesire>()
        desires.forEach { recruitDesires.add(RecruitDesire.fromType(it)) }

        return Recruit(
            id,
            firstName,
            lastName,
            position,
            type,
            recruitDesires,
            rating,
            potential,
            isCommitted,
            teamCommittedTo,
            interest
        )
    }

    companion object {
        fun from(recruit: Recruit): RecruitEntity {
            val desires = mutableListOf<Int>()
            recruit.desires.forEach { desires.add(it.type) }

            return RecruitEntity(
                recruit.id,
                recruit.firstName,
                recruit.lastName,
                recruit.position,
                recruit.playerType.type,
                desires,
                recruit.rating,
                recruit.potential,
                recruit.teamCommittedTo,
                recruit.isCommitted
            )
        }
    }
}