package com.appdev.jphil.basketballcoach.recruiting

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecruitRepository @Inject constructor(
    @TeamId private val teamId: Int,
    private val database: BasketballDatabase
) : RecruitContract.Repository {

    private lateinit var presenter: RecruitContract.Presenter

    override suspend fun loadRecruits(): Team {
        return TeamDatabaseHelper.loadTeamById(teamId, database) ?: throw IllegalStateException(
            "No team exists with teamId = $teamId"
        )
    }

    override suspend fun saveRecruits(recruits: List<Recruit>) {
        RecruitDatabaseHelper.saveRecruits(recruits, database)
    }

    override fun attachPresenter(presenter: RecruitContract.Presenter) {
        this.presenter = presenter
    }
}
