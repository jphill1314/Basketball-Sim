package com.appdev.jphil.basketballcoach.recruitingcompose

import com.appdev.jphil.basketball.recruits.Recruit
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
                team = dataState.team.getState(),
                recruits = dataState.recruits
                    .filter { filters.isEmpty() || filters.contains(it.position) }
                    .sortedWith(dataState.sortType.getComparable(teamId))
                    .map { it.getModel(teamId) }
            )
        }
    }

    private fun Team.getState() = TeamStateModel(
        returningPGs = getReturningPlayersAtPosition(1),
        committedPGs = getCommitsAtPosition(1),
        returningSGs = getReturningPlayersAtPosition(2),
        committedSGs = getCommitsAtPosition(2),
        returningSFs = getReturningPlayersAtPosition(3),
        committedSFs = getCommitsAtPosition(3),
        returningPFs = getReturningPlayersAtPosition(4),
        committedPFs = getCommitsAtPosition(4),
        returningCs = getReturningPlayersAtPosition(5),
        committedCs = getCommitsAtPosition(5),
    )

    private fun Recruit.getModel(teamId: Int) = RecruitModel(
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
        getInterest(teamId),
        status = ""
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
}
