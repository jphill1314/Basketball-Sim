package com.appdev.jphil.basketballcoach.playeroverview

import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.player.PlayerDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.PlayerId
import javax.inject.Inject

class PlayerOverviewRepository @Inject constructor(
    @PlayerId private val playerId: Int,
    private val database: BasketballDatabase
) : PlayerOverviewContract.Repository {

    private lateinit var presenter: PlayerOverviewContract.Presenter

    override suspend fun fetchPlayerAndStats(): PlayerOverviewModel {
        val player = PlayerDatabaseHelper.loadPlayerById(playerId, database)
        val stats = PlayerDatabaseHelper.loadGameStatsForPlayer(playerId, database)
        return PlayerOverviewModel(player, stats)
    }

    override fun attachPresenter(presenter: PlayerOverviewContract.Presenter) {
        this.presenter = presenter
    }
}
