package com.appdev.jphil.basketballcoach.recruitoverview

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.RecruitId
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import javax.inject.Inject

class RecruitOverviewRepository @Inject constructor(
    @RecruitId private val recruitId: Int,
    @TeamId private val teamId: Int,
    private val database: BasketballDatabase
) : RecruitOverviewContract.Repository {

    private lateinit var presenter: RecruitOverviewContract.Presenter

    override suspend fun loadRecruit(): RecruitOverviewModel {
        val recruit = RecruitDatabaseHelper.loadRecruitWithId(recruitId, database)
        val team = TeamDatabaseHelper.loadTeamById(teamId, database) ?: throw IllegalStateException(
            "No team exists for teamId = $teamId"
        )
        return RecruitOverviewModel(team, recruit)
    }

    override suspend fun saveRecruit(recruit: Recruit) {
        RecruitDatabaseHelper.saveRecruits(listOf(recruit), database)
    }

    override fun attachPresenter(presenter: RecruitOverviewContract.Presenter) {
        this.presenter = presenter
    }
}
