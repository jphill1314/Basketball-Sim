package com.appdev.jphil.basketballcoach.playeroverview

import android.widget.AdapterView
import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketballcoach.MVPContract
import com.appdev.jphil.basketballcoach.database.player.GameStatsEntity

interface PlayerOverviewContract {

    interface View : MVPContract.View {
        fun addPlayerInfo(player: Player)
        fun addPlayerStats(stats: List<GameStatsEntity>)
        fun displayPlayerInfo()
        fun displayPlayerStats()
    }

    interface Presenter : MVPContract.Presenter<View>, AdapterView.OnItemSelectedListener {
        fun onPlayerLoaded(player: Player)
        fun onStatsLoaded(stats: List<GameStatsEntity>)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun fetchPlayer()
        fun fetchPlayerStats()
    }
}