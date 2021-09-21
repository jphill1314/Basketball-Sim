package com.appdev.jphil.basketballcoach.coachoverview

import com.appdev.jphil.basketballcoach.database.coach.CoachDao
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.CoachId
import javax.inject.Inject

class CoachOverviewRepository @Inject constructor(
    @CoachId private val coachId: Int,
    private val coachDao: CoachDao
) : CoachOverviewContract.Repository {

    private lateinit var presenter: CoachOverviewContract.Presenter

    override suspend fun loadCoach(): CoachEntity {
        return coachDao.getCoachById(coachId)
    }

    override fun attachPresenter(presenter: CoachOverviewContract.Presenter) {
        this.presenter = presenter
    }
}
