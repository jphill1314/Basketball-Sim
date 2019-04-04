package com.appdev.jphil.basketballcoach.tournament

import android.content.res.Resources
import com.appdev.jphil.basketball.TenTeamTournament
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.schedule.ScheduleDataModel
import com.appdev.jphil.basketballcoach.util.RecordUtil
import javax.inject.Inject

class TournamentPresenter @Inject constructor(
    private val repository: TournamentContract.Repository,
    private val resources: Resources
) : TournamentContract.Presenter {

    private var view: TournamentContract.View? = null

    init {
        repository.attachPresenter(this)
    }

    override fun onTournamentLoaded(tournament: TenTeamTournament, allGames: List<GameEntity>) {
        val dataModels = mutableListOf<ScheduleDataModel>()
        tournament.games.forEach { game ->
            val homeTeamRecord = RecordUtil.getRecordAsPair(allGames, game.homeTeam.teamId)
            val homeRecord = resources.getString(R.string.standings_dash, homeTeamRecord.first, homeTeamRecord.second)
            val awayTeamRecord = RecordUtil.getRecordAsPair(allGames, game.awayTeam.teamId)
            val awayRecord = resources.getString(R.string.standings_dash, awayTeamRecord.first, awayTeamRecord.second)

            dataModels.add(ScheduleDataModel(
                    game.id ?: 0,
                    game.homeTeam.name,
                    homeRecord,
                    game.homeScore,
                    game.awayTeam.name,
                    awayRecord,
                    game.awayScore,
                    game.isFinal,
                    game.inProgress
            ))
        }
        view?.onTournamentLoaded(dataModels)
    }

    override fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean) {
        view?.startGameFragment(gameId, homeName, awayName, userIsHomeTeam)
    }

    override fun onFABClicked() {
        repository.simToGame()
    }

    override fun onViewAttached(view: TournamentContract.View) {
        this.view = view
        repository.fetchData()
    }

    override fun onViewDetached() {
        view = null
    }

    override fun onDestroyed() {

    }
}