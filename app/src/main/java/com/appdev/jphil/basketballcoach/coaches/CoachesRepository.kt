package com.appdev.jphil.basketballcoach.coaches

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.coach.CoachDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import javax.inject.Inject

class CoachesRepository @Inject constructor(
    @TeamId private val teamId: Int,
    private val database: BasketballDatabase
) : CoachesContract.Repository {

    private lateinit var presenter: CoachesContract.Presenter

    override suspend fun loadCoaches(): List<Coach> {
        return CoachDatabaseHelper.loadAllCoachesByTeamId(teamId, database)
    }

    override fun attachPresenter(presenter: CoachesContract.Presenter) {
        this.presenter = presenter
    }
}
