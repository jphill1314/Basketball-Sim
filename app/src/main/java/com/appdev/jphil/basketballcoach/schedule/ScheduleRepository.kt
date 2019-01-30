package com.appdev.jphil.basketballcoach.schedule

import com.appdev.jphil.basketball.Game
import com.appdev.jphil.basketballcoach.database.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val dbHelper: DatabaseHelper
) : ScheduleContract.Repository {

    private lateinit var presenter: ScheduleContract.Presenter
    private var games: List<Game>? = null

    override fun fetchSchedule() {
        GlobalScope.launch(Dispatchers.Main) {
            if (games == null) {
                val job = launch(Dispatchers.IO) {
                    games = dbHelper.loadGamesForTeam(1)
                }
                job.join()
                presenter.onScheduleLoaded(games!!)
            } else {
                presenter.onScheduleLoaded(games!!)
            }
        }
    }

    override fun attachPresenter(presenter: ScheduleContract.Presenter) {
        this.presenter = presenter
    }
}