package com.appdev.jphil.basketballcoach.recruiting

import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketball.recruits.Recruit
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

    override fun loadRecruits() {
        GlobalScope.launch(Dispatchers.IO) {
            val recruits = RecruitDatabaseHelper.loadAllRecruits(database)
            val team = TeamDatabaseHelper.loadTeamById(teamId, database)
            team?.let {
                if (recruits.first().interestInTeams.isEmpty()) {
                    generateInitialInterest(recruits, it)
                }
                withContext(Dispatchers.Main) {
                    presenter.onRecruitsLoaded(recruits, it)
                }
            }
        }
    }

    override fun saveRecruits(recruits: List<Recruit>) {
        GlobalScope.launch(Dispatchers.IO) {
            RecruitDatabaseHelper.saveRecruits(recruits, database)
        }
    }

    override fun attachPresenter(presenter: RecruitContract.Presenter) {
        this.presenter = presenter
    }

    private fun generateInitialInterest(recruits: List<Recruit>, team: Team): List<Recruit> {
        recruits.forEach { it.generateInitialInterest(team) }
        RecruitDatabaseHelper.saveRecruits(recruits, database)
        return recruits
    }
}