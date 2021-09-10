package com.appdev.jphil.basketballcoach.recruitoverview.ui

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.recruits.NewRecruitInterest
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team

interface RecruitOverviewContract {

    interface Interactor {
        fun startActiveRecruitment()
        fun stopActiveRecruitment()
        fun gainCommitment()
        fun selectCoachForRecruitment(coach: Coach)
        fun startRecruitmentWithCoach(coach: Coach, recruitToRemove: Recruit)
        fun onDialogDismissed()
    }

    data class DataState(
        val recruit: Recruit? = null,
        val team: Team? = null,
        val showDialog: Boolean = false,
        val coachForDialog: Coach? = null
    ) : com.appdev.jphil.basketballcoach.compose.arch.DataState

    data class ViewState(
        val isLoading: Boolean = true,
        val teamName: String = "",
        val teamRating: Int = 0,
        val isActivelyRecruited: Boolean = false,
        val recruit: Recruit? = null,
        val recruitInterest: NewRecruitInterest? = null,
        val coaches: List<Coach> = emptyList(),
        val coachForDialog: Coach? = null
    ) : com.appdev.jphil.basketballcoach.compose.arch.ViewState
}
