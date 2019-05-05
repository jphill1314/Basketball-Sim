package com.appdev.jphil.basketballcoach.playeroverview

import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.player.PlayerDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.PlayerId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayerOverviewRepository @Inject constructor(
    @PlayerId private val playerId: Int,
    private val database: BasketballDatabase
) : PlayerOverviewContract.Repository {

    private lateinit var presenter: PlayerOverviewContract.Presenter

    override fun fetchPlayerAndStats() {
        GlobalScope.launch(Dispatchers.IO) {
            val player = PlayerDatabaseHelper.loadPlayerById(playerId, database)
            val stats = PlayerDatabaseHelper.loadGameStatsForPlayer(playerId, database)
            withContext(Dispatchers.Main) {
                presenter.onPlayerLoaded(player, stats)
            }
        }
    }

    override fun attachPresenter(presenter: PlayerOverviewContract.Presenter) {
        this.presenter = presenter
    }
}