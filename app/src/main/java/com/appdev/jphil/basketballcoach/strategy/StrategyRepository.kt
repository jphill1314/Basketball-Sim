package com.appdev.jphil.basketballcoach.strategy

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.coach.CoachDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StrategyRepository @Inject constructor(
    @TeamId private val teamId: Int,
    private val database: BasketballDatabase
): StrategyContract.Repository {

    private lateinit var presenter: StrategyContract.Presenter

    override fun loadStrategy() {
        GlobalScope.launch(Dispatchers.IO) {
            val coach = CoachDatabaseHelper.loadHeadCoachByTeamId(teamId, database)
            withContext(Dispatchers.Main) {
                presenter.onStrategyLoaded(coach)
            }
        }
    }

    override fun saveStrategy(coach: Coach) {
        GlobalScope.launch(Dispatchers.IO) {
            CoachDatabaseHelper.saveCoach(coach, database)
        }
    }

    override fun attachPresenter(presenter: StrategyContract.Presenter) {
        this.presenter = presenter
    }
}