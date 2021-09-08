package com.appdev.jphil.basketballcoach.recruitoverviewcompose

import com.appdev.jphil.basketball.coaches.CoachType
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.compose.arch.Transformer
import javax.inject.Inject

class RecruitOverviewTransformer @Inject constructor() :
    Transformer<RecruitOverviewContract.DataState, RecruitOverviewContract.ViewState> {

    override fun transform(
        dataState: RecruitOverviewContract.DataState
    ): RecruitOverviewContract.ViewState {
        val recruit = dataState.recruit
        val team = dataState.team

        return if (recruit == null || team == null) {
            RecruitOverviewContract.ViewState(isLoading = true)
        } else {
            RecruitOverviewContract.ViewState(
                isLoading = false,
                teamName = team.schoolName,
                isActivelyRecruited = isActiveRecruitment(team, recruit),
                recruit = recruit,
                recruitInterest = recruit.recruitInterests.first { it.teamId == team.teamId },
                coaches = if (dataState.showDialog) {
                    team.coaches.filter { it.type != CoachType.HEAD_COACH }
                } else {
                    emptyList()
                },
                coachForDialog = dataState.coachForDialog
            )
        }
    }

    private fun isActiveRecruitment(team: Team, recruit: Recruit): Boolean {
        team.coaches.forEach { coach ->
            if (coach.recruitingAssignments.contains(recruit)) {
                return true
            }
        }
        return false
    }
}
