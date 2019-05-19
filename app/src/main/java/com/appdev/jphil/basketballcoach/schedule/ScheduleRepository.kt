package com.appdev.jphil.basketballcoach.schedule

import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    @TeamId private val teamId: Int,
    private val database: BasketballDatabase
) : ScheduleContract.Repository {

    private lateinit var presenter: ScheduleContract.Presenter

    override fun fetchSchedule() {
        GlobalScope.launch(Dispatchers.IO) {
            val gameEntities = GameDatabaseHelper.loadAllGameEntities(database).filter { it.tournamentId == null }
            val team = TeamDatabaseHelper.loadUserTeam(database)
            withContext(Dispatchers.Main) {
                presenter.onScheduleLoaded(gameEntities, team?.teamId == teamId)
            }
        }
    }

    override fun attachPresenter(presenter: ScheduleContract.Presenter) {
        this.presenter = presenter
    }
}