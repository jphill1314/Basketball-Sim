package com.appdev.jphil.basketballcoach.database.recruit

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.appdev.jphil.basketball.location.Location
import com.appdev.jphil.basketball.players.PlayerType
import com.appdev.jphil.basketball.recruits.Recruit

@Entity
data class RecruitEntity(
    @PrimaryKey val id: Int,
    val firstName: String,
    val lastName: String,
    val position: Int,
    val playerType: PlayerType,
    val rating: Int,
    val potential: Int,
    val teamCommittedTo: Int,
    val isCommitted: Boolean,
    val location: Location
) {

    fun createRecruit(teamInterest: List<RecruitInterestEntity>): Recruit {
        return Recruit(
            id,
            firstName,
            lastName,
            position,
            playerType,
            rating,
            potential,
            isCommitted,
            teamCommittedTo,
            location,
            teamInterest.map { it.create() }.toMutableList()
        )
    }

    companion object {
        fun from(recruit: Recruit): RecruitEntity {
            return RecruitEntity(
                recruit.id,
                recruit.firstName,
                recruit.lastName,
                recruit.position,
                recruit.playerType,
                recruit.rating,
                recruit.potential,
                recruit.teamCommittedTo,
                recruit.isCommitted,
                recruit.location
            )
        }
    }
}
