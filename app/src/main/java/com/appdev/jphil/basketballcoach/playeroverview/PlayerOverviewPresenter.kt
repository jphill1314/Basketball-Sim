package com.appdev.jphil.basketballcoach.playeroverview

import android.view.View
import android.widget.AdapterView
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketballcoach.arch.BasePresenter
import com.appdev.jphil.basketballcoach.arch.DispatcherProvider
import com.appdev.jphil.basketballcoach.database.player.GameStatsEntity
import com.appdev.jphil.basketballcoach.util.StatsUtil
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerOverviewPresenter @Inject constructor(
    private val repository: PlayerOverviewContract.Repository,
    dispatcherProvider: DispatcherProvider
) : BasePresenter(dispatcherProvider), PlayerOverviewContract.Presenter {

    private var view: PlayerOverviewContract.View? = null
    private val stats = mutableListOf<GameStatsEntity>()
    private lateinit var player: Player

    init {
        repository.attachPresenter(this)
    }

    override fun onViewAttached(view: PlayerOverviewContract.View) {
        this.view = view
        coroutineScope.launch {
            val model = repository.fetchPlayerAndStats()
            player = model.player
            view.addPlayerInfo(player, StatsUtil().apply { calculateTotals(model.stats) })
            onStatsLoaded(model.stats)
        }
    }

    override fun onViewDetached() {
        view = null
    }

    private fun onStatsLoaded(stats: List<GameStatsEntity>) {
        this.stats.clear()
        this.stats.addAll(stats)
        var timePlayed = 0
        var twoPointAttempts = 0
        var twoPointMakes = 0
        var threePointAttempts = 0
        var threePointMakes = 0
        var assists = 0
        var offensiveRebounds = 0
        var defensiveRebounds = 0
        var turnovers = 0
        var steals = 0
        var fouls = 0
        var freeThrowShots = 0
        var freeThrowMakes = 0

        stats.forEach {
            timePlayed += it.timePlayed
            twoPointAttempts += it.twoPointAttempts
            twoPointMakes += it.twoPointMakes
            threePointAttempts += it.threePointAttempts
            threePointMakes += it.threePointMakes
            assists += it.assists
            offensiveRebounds += it.offensiveRebounds
            defensiveRebounds += it.defensiveRebounds
            turnovers += it.turnovers
            steals += it.steals
            fouls += it.fouls
            freeThrowShots += it.freeThrowShots
            freeThrowMakes += it.freeThrowMakes
        }

        this.stats.add(
            GameStatsEntity(
                -1,
                -1,
                -1,
                -1,
                "Career",
                true,
                0,
                0,
                timePlayed,
                twoPointAttempts,
                twoPointMakes,
                threePointAttempts,
                threePointMakes,
                assists,
                offensiveRebounds,
                defensiveRebounds,
                turnovers,
                steals,
                fouls,
                freeThrowShots,
                freeThrowMakes
            )
        )

        view?.addPlayerStats(this.stats)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) { }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> this.view?.displayPlayerInfo()
            else -> this.view?.displayPlayerStats()
        }
    }
}
