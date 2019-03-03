package com.appdev.jphil.basketballcoach.strategy

import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketballcoach.database.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StrategyRepository @Inject constructor(
    private val teamId: Int,
    private val dbHelper: DatabaseHelper
): StrategyContract.Repository {

    private lateinit var presenter: StrategyContract.Presenter

    override fun loadStrategy() {
        GlobalScope.launch(Dispatchers.IO) {
            val team = dbHelper.loadTeamById(teamId)
            team?.let {
                withContext(Dispatchers.Main) {
                    presenter.onStrategyLoaded(it)
                }
            }
        }
    }

    override fun saveStrategy(team: Team) {
        GlobalScope.launch(Dispatchers.IO) {
            dbHelper.saveTeam(team)
        }
    }

    override fun attachPresenter(presenter: StrategyContract.Presenter) {
        this.presenter = presenter
    }
}