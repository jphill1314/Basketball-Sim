package com.appdev.jphil.basketballcoach.recruitoverview.ui

import androidx.lifecycle.viewModelScope
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.compose.arch.ComposePresenter
import com.appdev.jphil.basketballcoach.compose.arch.Transformer
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.RecruitId
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import com.appdev.jphil.basketballcoach.recruitoverview.data.RecruitRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecruitOverviewPresenter(
    transformer: RecruitOverviewTransformer,
    private val params: Params,
    private val recruitRepository: RecruitRepository
) : ComposePresenter<RecruitOverviewContract.DataState, RecruitOverviewContract.ViewState>(),
    RecruitOverviewContract.Interactor,
    Transformer<RecruitOverviewContract.DataState, RecruitOverviewContract.ViewState> by transformer {

    data class Params @Inject constructor(
        @TeamId val teamId: Int,
        @RecruitId val recruitId: Int
    )

    override val initialDataState = RecruitOverviewContract.DataState()

    init {
        viewModelScope.launch {
            recruitRepository.getRecruitAndTeam(params.teamId, params.recruitId).collect {
                updateState {
                    copy(
                        recruit = it.recruit,
                        team = it.team
                    )
                }
            }
        }
    }

    override fun startActiveRecruitment() {
        updateState { copy(showDialog = true) }
    }

    override fun selectCoachForRecruitment(coach: Coach) {
        if (coach.recruitingAssignments.size == 5) {
            updateState { copy(coachForDialog = coach) }
        } else {
            runIfPossible { _, recruit ->
                coach.recruitingAssignments.add(recruit)
                viewModelScope.launch {
                    recruitRepository.updateCoach(coach)
                }
                updateState { copy(showDialog = false) }
            }
        }
    }

    override fun startRecruitmentWithCoach(coach: Coach, recruitToRemove: Recruit) {
        runIfPossible { _, recruit ->
            coach.recruitingAssignments.remove(recruitToRemove)
            coach.recruitingAssignments.add(recruit)
            viewModelScope.launch {
                recruitRepository.updateCoach(coach)
            }
            updateState {
                copy(
                    showDialog = false,
                    coachForDialog = null
                )
            }
        }
    }

    override fun onDialogDismissed() {
        updateState {
            copy(
                showDialog = false,
                coachForDialog = null
            )
        }
    }

    override fun stopActiveRecruitment() {
        runIfPossible { team, recruit ->
            team.coaches.forEach { coach ->
                if (coach.recruitingAssignments.contains(recruit)) {
                    coach.recruitingAssignments.remove(recruit)
                    viewModelScope.launch {
                        recruitRepository.updateCoach(coach)
                    }
                }
            }
        }
    }

    override fun gainCommitment() {
        runIfPossible { team, recruit ->
            if (recruit.getInterest(params.teamId) >= 100) {
                recruit.apply {
                    isCommitted = true
                    teamCommittedTo = params.teamId
                }
                viewModelScope.launch {
                    recruitRepository.updateRecruit(recruit)

                    team.commitments.add(recruit)
                    recruitRepository.updateTeam(team)

                    team.coaches.forEach { coach ->
                        if (coach.recruitingAssignments.contains(recruit)) {
                            coach.recruitingAssignments.remove(recruit)
                            recruitRepository.updateCoach(coach)
                        }
                    }
                }
            } else {
                // TODO: notify user that nothing happened
            }
        }
    }

    private fun runIfPossible(block: (Team, Recruit) -> Unit) {
        val recruit = dataState.value.recruit
        val team = dataState.value.team

        if (recruit != null && team != null) {
            block(team, recruit)
        }
    }
}
