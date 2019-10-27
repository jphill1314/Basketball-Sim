package com.appdev.jphil.basketballcoach.schedule

import android.content.res.Resources
import com.appdev.jphil.basketball.datamodels.ScheduleDataModel
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import com.appdev.jphil.basketballcoach.newseason.NewSeasonRepository
import com.appdev.jphil.basketballcoach.simdialog.SimDialogDataModel
import com.appdev.jphil.basketballcoach.simulation.SimulationContract
import com.appdev.jphil.basketballcoach.tracking.TrackingKeys
import com.appdev.jphil.basketballcoach.util.RecordUtil
import com.flurry.android.FlurryAgent
import javax.inject.Inject

class SchedulePresenter @Inject constructor(
    @TeamId private val teamId: Int,
    private val resources: Resources,
    private val repository: ScheduleContract.Repository,
    private val gameSimRepository: SimulationContract.GameSimRepository,
    private val newSeasonRepository: NewSeasonRepository
): ScheduleContract.Presenter {

    init {
        repository.attachPresenter(this)
        gameSimRepository.attachPresenter(this)
    }

    private var view: ScheduleContract.View? = null
    private var teamGames = listOf<GameEntity>()
    private var isSimming = false

    override fun fetchSchedule() {
        repository.fetchSchedule()
    }

    override fun updateSchedule(finishedGame: Game) {
        fetchSchedule()
        view?.updateProgressBar(SimDialogDataModel(
            finishedGame.homeTeam.schoolName,
            finishedGame.awayTeam.schoolName,
            finishedGame.homeScore,
            finishedGame.awayScore
        ))
    }

    override fun onSimCompleted() {
        isSimming = false
        fetchSchedule()
    }

    override fun onSeasonCompleted(conferenceTournamentIsFinished: Boolean) {
        if (conferenceTournamentIsFinished) {
            // Season is over start new season
            view?.showProgressBar()
            newSeasonRepository.startNewSeason {
                fetchSchedule()
                isSimming = false
            }
            FlurryAgent.logEvent(
                TrackingKeys.EVENT_TAP,
                mapOf(TrackingKeys.PAYLOAD_TAP_TYPE to TrackingKeys.VALUE_START_NEW_SEASON)
            )
        } else {
            // Season is not over, open tournament view
            view?.goToConferenceTournament()
        }
    }

    override fun onScheduleLoaded(games: List<GameEntity>, isUsersSchedule: Boolean) {
        teamGames = games.filter { it.homeTeamId == teamId || it.awayTeamId == teamId }
        val dataModels = mutableListOf<ScheduleDataModel>()
        teamGames.forEach { game ->
            val homeTeamRecord = RecordUtil.getRecordAsPair(games, game.homeTeamId)
            val homeRecord = resources.getString(R.string.standings_dash, homeTeamRecord.first, homeTeamRecord.second)
            val awayTeamRecord = RecordUtil.getRecordAsPair(games, game.awayTeamId)
            val awayRecord = resources.getString(R.string.standings_dash, awayTeamRecord.first, awayTeamRecord.second)

            dataModels.add(
                ScheduleDataModel(
                    game.id ?: 0,
                    game.homeTeamName,
                    homeRecord,
                    game.homeScore,
                    game.awayTeamName,
                    awayRecord,
                    game.awayScore,
                    game.isFinal,
                    game.inProgress
                )
            )
        }
        if (!isUsersSchedule) {
            view?.disableFab()
        } else if(!isSimming) {
            view?.enableFab()
        }

        if (!isSimming) {
            view?.hideProgressBar()
        }
        view?.displaySchedule(dataModels, isUsersSchedule)

    }

    override fun onFABClicked() {
        view?.disableFab()
        view?.showProgressBar()
        isSimming = true
        gameSimRepository.startNextGame()
    }

    override fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean) {
        FlurryAgent.logEvent(TrackingKeys.EVENT_TAP, mapOf(TrackingKeys.PAYLOAD_TAP_TYPE to TrackingKeys.VALUE_PLAY_GAME))
        view?.startGameFragment(gameId, homeName, awayName, userIsHomeTeam)
    }

    override fun simulateToGame(gameId: Int) {
        view?.showProgressBar()
        isSimming = true
        gameSimRepository.simToGame(gameId)
        FlurryAgent.logEvent(TrackingKeys.EVENT_TAP, mapOf(TrackingKeys.PAYLOAD_TAP_TYPE to TrackingKeys.VALUE_SIM_TO_GAME))
    }

    override fun simulateGame(gameId: Int) {
        view?.showProgressBar()
        isSimming = true
        gameSimRepository.simGame(gameId)
        FlurryAgent.logEvent(TrackingKeys.EVENT_TAP, mapOf(TrackingKeys.PAYLOAD_TAP_TYPE to TrackingKeys.VALUE_SIM_GAME))
    }

    override fun goToConferenceTournament() {
        if (teamGames.filter { it.isFinal }.size == teamGames.size) {
            view?.showProgressBar()
            gameSimRepository.finishSeason()
        }
    }

    override fun onViewAttached(view: ScheduleContract.View) {
        this.view = view
        fetchSchedule()
    }

    override fun onViewDetached() {
        view = null
    }
}