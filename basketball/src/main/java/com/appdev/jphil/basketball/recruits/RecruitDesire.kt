package com.appdev.jphil.basketball.recruits

import com.appdev.jphil.basketball.players.PlayerType
import com.appdev.jphil.basketball.teams.Team
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

enum class RecruitDesire(val type: Int) {
    STAR(0),
    DEVELOP(1),
    GOOD_FIT(3);
}

data class RecruitDesireData(
    val preferredPrestige: Int,
    val wantsClose: Boolean,
    val wantsFar: Boolean,
    val wantsImmediateStart: Boolean,
    val wantsToDevelop: Boolean,
    val wantsThrees: Boolean,
    val wantsPress: Boolean,
    val wantsAggressive: Boolean,
    val wantsToBeStar: Boolean,
) {

    fun createInterest(team: Team) = NewRecruitInterest(
        null,
        team.teamId,
        preferredPrestige,
        wantsClose,
        wantsFar,
        wantsImmediateStart,
        wantsToDevelop,
        wantsThrees,
        wantsPress,
        wantsAggressive,
        wantsToBeStar
    )

    companion object {
        fun from(desire: RecruitDesire, recruit: Recruit): RecruitDesireData {
//            val prestige = ((recruit.rating + recruit.potential) / 2.0).toInt() + Random.nextInt(40) - 20
            val prestige = when (desire) {
                RecruitDesire.STAR -> min(90, recruit.rating) - Random.nextInt(20)
                RecruitDesire.GOOD_FIT -> min(90, ((recruit.rating + recruit.potential) / 2.0).toInt())
                RecruitDesire.DEVELOP -> min(80, recruit.potential) + Random.nextInt(20)
            }
            val locationMatters = Random.nextInt(3)

            val wantsThrees = when (recruit.playerType) {
                PlayerType.SHOOTER -> Random.nextInt(100) < 75
                PlayerType.DISTRIBUTOR -> recruit.position == 1 && Random.nextInt(100) < 66
                else -> false
            }

            val wantsPress = when (recruit.playerType) {
                PlayerType.DEFENDER -> Random.nextBoolean()
                else -> false
            }

            val wantsAggressive = when (recruit.playerType) {
                PlayerType.DEFENDER -> Random.nextBoolean()
                PlayerType.REBOUNDER -> Random.nextBoolean()
                else -> false
            }

            return RecruitDesireData(
                preferredPrestige = max(0, min(90, prestige)),
                wantsClose = locationMatters == 0,
                wantsFar = locationMatters == 1,
                wantsImmediateStart = desire == RecruitDesire.STAR,
                wantsToDevelop = desire == RecruitDesire.DEVELOP,
                wantsThrees = wantsThrees,
                wantsPress = wantsPress,
                wantsAggressive = wantsAggressive,
                wantsToBeStar = desire == RecruitDesire.STAR
            )
        }
    }
}
