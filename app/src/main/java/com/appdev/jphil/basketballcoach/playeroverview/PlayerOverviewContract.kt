package com.appdev.jphil.basketballcoach.playeroverview

import android.widget.AdapterView
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketballcoach.MVPContract
import com.appdev.jphil.basketballcoach.database.player.GameStatsEntity
import com.appdev.jphil.basketballcoach.util.StatsUtil

interface PlayerOverviewContract {

    interface View : MVPContract.View {
        fun addPlayerInfo(player: Player, stats: StatsUtil)
        fun addPlayerStats(stats: List<GameStatsEntity>)
        fun displayPlayerInfo()
        fun displayPlayerStats()
    }

    interface Presenter : MVPContract.Presenter<View>, AdapterView.OnItemSelectedListener

    interface Repository : MVPContract.Repository<Presenter> {
        suspend fun fetchPlayerAndStats(): PlayerOverviewModel
    }
}
