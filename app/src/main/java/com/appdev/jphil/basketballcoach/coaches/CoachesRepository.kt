package com.appdev.jphil.basketballcoach.coaches

import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.coach.CoachDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CoachesRepository @Inject constructor(
    @TeamId private val teamId: Int,
    private val database: BasketballDatabase
) : CoachesContract.Repository {

    private lateinit var presenter: CoachesContract.Presenter

    override fun loadCoaches() {
        GlobalScope.launch(Dispatchers.IO) {
            val coaches = CoachDatabaseHelper.loadAllCoachesByTeamId(teamId, database)
            withContext(Dispatchers.Main) {
                presenter.onCoachesLoaded(coaches)
            }
        }
    }

    override fun attachPresenter(presenter: CoachesContract.Presenter) {
        this.presenter = presenter
    }
}