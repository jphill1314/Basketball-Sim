package com.appdev.jphil.basketballcoach.playeroverview

import com.appdev.jphil.basketballcoach.database.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayerOverviewRepository @Inject constructor(
    private val playerId: Int,
    private val dbHelper: DatabaseHelper
) : PlayerOverviewContract.Repository {

    private lateinit var presenter: PlayerOverviewContract.Presenter

    override fun fetchPlayer() {
        GlobalScope.launch(Dispatchers.IO) {
            val player = dbHelper.loadPlayerById(playerId)
            withContext(Dispatchers.Main) {
                presenter.onPlayerLoaded(player)
            }
        }
    }

    override fun fetchPlayerStats() {
        GlobalScope.launch(Dispatchers.IO) {
            val stats = dbHelper.loadGameStatsForPlayer(playerId)
            withContext(Dispatchers.Main) {
                presenter.onStatsLoaded(stats)
            }
        }
    }

    override fun attachPresenter(presenter: PlayerOverviewContract.Presenter) {
        this.presenter = presenter
    }
}