package com.appdev.jphil.basketball.recruits

import com.appdev.jphil.basketball.location.getRegion
import com.appdev.jphil.basketball.teams.Team

interface NewRecruitDesire {
    companion object {
        const val MAX_DESIRE = 20
    }

    fun teamMeetsDesire(team: Team, recruit: Recruit): Int
}

data class LocationDesire(
    val wantsClose: Boolean,
    val wantsFar: Boolean
) : NewRecruitDesire {

    override fun teamMeetsDesire(team: Team, recruit: Recruit): Int {
        return when {
            wantsClose -> when {
                team.location == recruit.location -> NewRecruitDesire.MAX_DESIRE
                team.location.getRegion() == recruit.location.getRegion() -> NewRecruitDesire.MAX_DESIRE / 2
                else -> 0
            }
            wantsFar -> when {
                team.location.getRegion() != recruit.location.getRegion() -> NewRecruitDesire.MAX_DESIRE
                team.location != recruit.location -> NewRecruitDesire.MAX_DESIRE / 2
                else -> 0
            }
            else -> NewRecruitDesire.MAX_DESIRE / 2
        }
    }
}

data class PrestigeDesire(
    val minPrestige: Int,
    val preferredPrestige: Int
) : NewRecruitDesire {

    override fun teamMeetsDesire(team: Team, recruit: Recruit): Int {
        return when {
            team.teamRating >= preferredPrestige -> NewRecruitDesire.MAX_DESIRE // TODO: siding scale between max and 0
            team.teamRating >= minPrestige -> NewRecruitDesire.MAX_DESIRE / 2
            else -> 0
        }
    }
}

data class PlayingTimeDesire(
    val wantsImmediateStart: Boolean,
    val wantsToDevelop: Boolean
) : NewRecruitDesire {

    override fun teamMeetsDesire(team: Team, recruit: Recruit): Int {
        val compition = team.players.filter {
            it.year < 3 && it.position == recruit.position
        }.sortedByDescending {
            it.getOverallRating()
        }

        return when {
            wantsImmediateStart -> when {
                recruit.rating > (compition.firstOrNull()?.getOverallRating() ?: 0) + 10 ->
                    NewRecruitDesire.MAX_DESIRE
                recruit.rating > (compition.getOrNull(1)?.getOverallRating() ?: 0) + 10 ->
                    NewRecruitDesire.MAX_DESIRE / 2
                else -> 0
            }
            wantsToDevelop -> {
                when {
                    recruit.rating > (compition.firstOrNull()?.getOverallRating() ?: 0) + 10 -> 0
                    recruit.rating > (compition.getOrNull(1)?.getOverallRating() ?: 0) + 10 ->
                        NewRecruitDesire.MAX_DESIRE / 2
                    else -> NewRecruitDesire.MAX_DESIRE
                }
            }
            else -> NewRecruitDesire.MAX_DESIRE / 2
        }
    }
}

data class PlayStyleDesire(
    val wantsThrees: Boolean,
    val wantsPress: Boolean,
    val wantsAggressive: Boolean
) : NewRecruitDesire {

    override fun teamMeetsDesire(team: Team, recruit: Recruit): Int {
        val coach = team.getHeadCoach()
        return when {
            wantsThrees -> when {
                coach.offenseFavorsThrees > 70 -> NewRecruitDesire.MAX_DESIRE
                coach.offenseFavorsThrees > 50 -> NewRecruitDesire.MAX_DESIRE / 2
                else -> 0
            }
            wantsPress -> when {
                coach.pressFrequency > 70 -> NewRecruitDesire.MAX_DESIRE
                coach.pressFrequency > 50 -> NewRecruitDesire.MAX_DESIRE / 2
                else -> 0
            }
            wantsAggressive -> when {
                coach.aggression > 70 -> NewRecruitDesire.MAX_DESIRE
                coach.aggression > 50 -> NewRecruitDesire.MAX_DESIRE / 2
                else -> 0
            }
            else -> NewRecruitDesire.MAX_DESIRE / 2
        }
    }
}

data class TeamFitDesire(
    val wantsToBeStar: Boolean,
    val wantsToDevelop: Boolean
) : NewRecruitDesire {

    override fun teamMeetsDesire(team: Team, recruit: Recruit): Int {
        return when {
            wantsToBeStar -> when {
                recruit.rating > team.teamRating + 10 -> NewRecruitDesire.MAX_DESIRE
                recruit.rating >= team.teamRating -> NewRecruitDesire.MAX_DESIRE / 2
                else -> 0
            }
            wantsToDevelop -> when {
                recruit.rating > team.teamRating -> 0
                recruit.rating >= team.teamRating - 10 -> NewRecruitDesire.MAX_DESIRE / 2
                else -> NewRecruitDesire.MAX_DESIRE
            }
            else -> NewRecruitDesire.MAX_DESIRE / 2
        }
    }
}
