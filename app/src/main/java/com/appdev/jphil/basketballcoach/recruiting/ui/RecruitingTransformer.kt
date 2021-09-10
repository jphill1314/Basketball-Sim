package com.appdev.jphil.basketballcoach.recruiting.ui

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.recruits.getActiveRecruitmentsAtPosition
import com.appdev.jphil.basketball.recruits.getCommitsAtPosition
import com.appdev.jphil.basketball.recruits.getReturningPlayersAtPosition
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.compose.arch.Transformer
import javax.inject.Inject

class RecruitingTransformer @Inject constructor() :
    Transformer<RecruitingContract.DataState, RecruitingContract.ViewState> {

    override fun transform(dataState: RecruitingContract.DataState): RecruitingContract.ViewState {
        return if (dataState.team == null) {
            RecruitingContract.ViewState(isLoading = true)
        } else {
            val teamId = dataState.team.teamId
            val filters = dataState.positionFilters
            RecruitingContract.ViewState(
                isLoading = false,
                showClearFilters = dataState.positionFilters.isNotEmpty(),
                positionFilters = dataState.positionFilters,
                onlyShowRecruiting = dataState.onlyShowRecruiting,
                team = dataState.team.getState(),
                recruits = dataState.recruits
                    .filter { filters.isEmpty() || filters.contains(it.position) }
                    .filter {
                        !dataState.onlyShowRecruiting || isActiveRecruitment(
                            dataState.team,
                            it
                        )
                    }
                    .sortedWith(dataState.sortType.getComparable(teamId))
                    .map { it.getModel(dataState.team) }
            )
        }
    }

    private fun Team.getState() = TeamStateModel(
        returningPGs = getReturningPlayersAtPosition(1),
        committedPGs = getCommitsAtPosition(1),
        recruitingPGs = getActiveRecruitmentsAtPosition(1),
        returningSGs = getReturningPlayersAtPosition(2),
        committedSGs = getCommitsAtPosition(2),
        recruitingSGs = getActiveRecruitmentsAtPosition(2),
        returningSFs = getReturningPlayersAtPosition(3),
        committedSFs = getCommitsAtPosition(3),
        recruitingSFs = getActiveRecruitmentsAtPosition(3),
        returningPFs = getReturningPlayersAtPosition(4),
        committedPFs = getCommitsAtPosition(4),
        recruitingPFs = getActiveRecruitmentsAtPosition(4),
        returningCs = getReturningPlayersAtPosition(5),
        committedCs = getCommitsAtPosition(5),
        recruitingCs = getActiveRecruitmentsAtPosition(5),
    )

    private fun Recruit.getModel(team: Team) = RecruitModel(
        id,
        fullName,
        position = when (position) {
            1 -> R.string.pg
            2 -> R.string.sg
            3 -> R.string.sf
            4 -> R.string.pf
            else -> R.string.c
        },
        playerType.type,
        rating,
        potential,
        getInterest(team.teamId),
        getRecruitmentLevel(team.teamId),
        status = when {
            isCommitted -> when (teamCommittedTo) {
                team.teamId -> R.string.committed_to_you
                else -> R.string.committed_elsewhere
            }
            isActiveRecruitment(team, this) -> R.string.recruiting
            else -> R.string.empty_string
        }
    )

    private fun SortType.getComparable(teamId: Int): Comparator<Recruit> = when (this) {
        SortType.MOST_INTEREST -> compareBy(
            { -it.getInterest(teamId) },
            { -it.rating }
        )
        SortType.LEAST_INTEREST -> compareBy(
            { it.getInterest(teamId) },
            { it.rating }
        )
        SortType.MOST_INTERACTION -> compareBy(
            { -it.getRecruitmentLevel(teamId) },
            { -it.getInterest(teamId) },
            { -it.rating }
        )
        SortType.LEAST_INTERACTION -> compareBy(
            { it.getRecruitmentLevel(teamId) },
            { it.getInterest(teamId) },
            { it.rating }
        )
        SortType.BEST_LAST -> compareBy { it.rating }
        else -> compareBy { -it.rating }
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
