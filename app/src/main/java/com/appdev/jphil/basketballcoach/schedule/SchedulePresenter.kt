package com.appdev.jphil.basketballcoach.schedule

import android.content.res.Resources
import com.appdev.jphil.basketball.datamodels.ScheduleDataModel
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.arch.BasePresenter
import com.appdev.jphil.basketballcoach.arch.DispatcherProvider
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.ConferenceId
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import com.appdev.jphil.basketballcoach.newseason.NewSeasonRepository
import com.appdev.jphil.basketballcoach.simdialog.SimDialogDataModel
import com.appdev.jphil.basketballcoach.simdialog.SimDialogState
import com.appdev.jphil.basketballcoach.simulation.SimulationContract
import com.appdev.jphil.basketballcoach.tracking.TrackingKeys
import com.appdev.jphil.basketballcoach.util.RecordUtil
import com.flurry.android.FlurryAgent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class SchedulePresenter @Inject constructor(
    @TeamId private val teamId: Int,
    @ConferenceId private val confId: Int,
    private val resources: Resources,
    private val repository: ScheduleContract.Repository,
    private val gameSimRepository: SimulationContract.GameSimRepository,
    private val newSeasonRepository: NewSeasonRepository,
    dispatcherProvider: DispatcherProvider
): BasePresenter(dispatcherProvider), ScheduleContract.Presenter {

    init {
        repository.attachPresenter(this)
        gameSimRepository.attachPresenter(this)
    }

    private var view: ScheduleContract.View? = null
    private var teamGames = listOf<GameEntity>()
    private var isSimming = false
    private var state: SimDialogState? = null
    private val simGames = mutableListOf<SimDialogDataModel>()
    private var totalGames = 0
    private var teamsSaved = 0

    override fun fetchSchedule() {
        coroutineScope.launch {
            val model = repository.fetchSchedule()
            val games = model.games

            teamGames = games.filter { it.homeTeamId == teamId || it.awayTeamId == teamId }
            val dataModels = mutableListOf<ScheduleDataModel>()
            teamGames.forEach { game ->
                val homeTeamRecord = RecordUtil.getRecordAsPair(games, game.homeTeamId)
                val homeRecord = resources.getString(
                    R.string.standings_dash,
                    homeTeamRecord.first,
                    homeTeamRecord.second
                )
                val awayTeamRecord = RecordUtil.getRecordAsPair(games, game.awayTeamId)
                val awayRecord = resources.getString(
                    R.string.standings_dash,
                    awayTeamRecord.first,
                    awayTeamRecord.second
                )

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
                        isVictory(game),
                        game.inProgress
                    )
                )
            }

            if (!isSimming) {
                view?.hideProgressBar()
            }
            view?.displaySchedule(dataModels, model.isUsersSchedule)
        }
    }

    override fun onSimulationStarted(totalGames: Int) {
        simGames.clear()
        teamsSaved = 1
        this.totalGames = totalGames + 1
        state = SimDialogState()
        state?.let {
            it.games.postValue(simGames)
            it.text.postValue(resources.getString(R.string.sim_game_x_of_y, 1, this.totalGames))
            it.canCancel.postValue(true)
            view?.setDialogState(it)
        }
    }

    override fun updateSchedule(finishedGame: Game) {
        fetchSchedule()
        simGames.add(0, SimDialogDataModel(
            finishedGame.homeTeam.schoolName,
            finishedGame.awayTeam.schoolName,
            finishedGame.homeScore,
            finishedGame.awayScore
        ))
        if (state == null) {
            onSimulationStarted(-1)
        }
        state?.games?.postValue(simGames)
        state?.text?.postValue(if (simGames.size == totalGames) {
            resources.getString(R.string.saving_sim_results)
        } else {
            resources.getString(R.string.sim_game_x_of_y, simGames.size + 1, totalGames)
        })
    }

    override fun onSimCompleted() {
        state = null
        isSimming = false
        fetchSchedule()
    }

    override fun onSeasonCompleted(conferenceTournamentIsFinished: Boolean) {
        if (conferenceTournamentIsFinished) {
            // Season is over start new season
            view?.showProgressBar()
            state = SimDialogState()
            state?.let {
                it.text.postValue(resources.getString(R.string.starting_new_season))
                view?.setDialogState(it)
            }
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
            view?.hideProgressBar()
            view?.goToConferenceTournament()
        }
    }

    private fun isVictory(game: GameEntity): Boolean {
        return if (game.isFinal) {
            if (game.homeTeamId == teamId) {
                game.homeScore > game.awayScore
            } else {
                game.awayScore > game.homeScore
            }
        } else {
            false
        }
    }

    override fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean) {
        view?.startGameFragment(gameId, homeName, awayName, userIsHomeTeam)
    }

    override fun playGame(gameId: Int) {
        view?.showProgressBar()
        isSimming = true
        gameSimRepository.playGame(gameId)
        FlurryAgent.logEvent(TrackingKeys.EVENT_TAP, mapOf(TrackingKeys.PAYLOAD_TAP_TYPE to TrackingKeys.VALUE_PLAY_GAME))
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
            isSimming = true
            gameSimRepository.finishRegularSeason()
        }
    }

    override fun finishSeason() {
        coroutineScope.launch {
            if (repository.tournamentIsOver(confId)) {
                view?.showProgressBar()
                state = SimDialogState().apply { text.postValue("Finish Season") }
                gameSimRepository.finishTournaments()
            }
        }
    }

    override fun cancelSim() {
        gameSimRepository.cancelSim()
        FlurryAgent.logEvent(TrackingKeys.EVENT_TAP, mapOf(TrackingKeys.PAYLOAD_TAP_TYPE to TrackingKeys.VALUE_CANCEL_SIM))
    }

    override fun onViewAttached(view: ScheduleContract.View) {
        this.view = view
        fetchSchedule()
        state?.let { view.setDialogState(it) }
    }

    override fun onViewDetached() {
        view = null
    }
}
