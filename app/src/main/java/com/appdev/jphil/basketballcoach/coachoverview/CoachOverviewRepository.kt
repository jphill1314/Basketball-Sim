package com.appdev.jphil.basketballcoach.coachoverview

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.coach.CoachDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.CoachId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CoachOverviewRepository @Inject constructor(
    @CoachId private val coachId: Int,
    private val database: BasketballDatabase
) : CoachOverviewContract.Repository {

    private lateinit var presenter: CoachOverviewContract.Presenter

    override suspend fun loadCoach(): Coach {
        return CoachDatabaseHelper.loadCoachById(coachId, database)
    }

    override suspend fun saveCoach(coach: Coach) {
        CoachDatabaseHelper.saveCoach(coach, database)
    }

    override fun attachPresenter(presenter: CoachOverviewContract.Presenter) {
        this.presenter = presenter
    }
}
