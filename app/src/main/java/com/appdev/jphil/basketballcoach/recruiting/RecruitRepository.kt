package com.appdev.jphil.basketballcoach.recruiting

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import javax.inject.Inject

class RecruitRepository @Inject constructor(
    @TeamId private val teamId: Int,
    private val database: BasketballDatabase
) : RecruitContract.Repository {

    private lateinit var presenter: RecruitContract.Presenter

    override suspend fun loadTeam(): Team {
        return TeamDatabaseHelper.loadTeamById(teamId, database) ?: throw IllegalStateException(
            "No team exists with teamId = $teamId"
        )
    }

    override suspend fun loadRecruits(): List<Recruit> {
        return database.relationalDao().loadAllRecruits().map {
            it.recruitEntity.createRecruit(it.recruitInterestEntities)
        }
    }

    override suspend fun saveRecruits(recruits: List<Recruit>) {
        RecruitDatabaseHelper.saveRecruits(recruits, database)
    }

    override fun attachPresenter(presenter: RecruitContract.Presenter) {
        this.presenter = presenter
    }
}
