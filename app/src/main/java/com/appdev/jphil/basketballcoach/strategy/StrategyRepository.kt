package com.appdev.jphil.basketballcoach.strategy

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.coach.CoachDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import javax.inject.Inject

class StrategyRepository @Inject constructor(
    @TeamId private val teamId: Int,
    private val database: BasketballDatabase
) : StrategyContract.Repository {

    private lateinit var presenter: StrategyContract.Presenter

    override suspend fun loadStrategy(): Coach {
        return CoachDatabaseHelper.loadHeadCoachByTeamId(teamId, database)
    }

    override suspend fun saveStrategy(coach: Coach) {
        CoachDatabaseHelper.saveCoach(coach, database)
    }

    override fun attachPresenter(presenter: StrategyContract.Presenter) {
        this.presenter = presenter
    }
}
