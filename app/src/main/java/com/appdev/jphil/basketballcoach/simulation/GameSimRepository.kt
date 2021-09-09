package com.appdev.jphil.basketballcoach.simulation

import com.appdev.jphil.basketballcoach.arch.DispatcherProvider
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.game.GameDao
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.database.relations.RelationalDao
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameSimRepository @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val database: BasketballDatabase,
    private val relationalDao: RelationalDao,
    private val gameDao: GameDao
) {
    private val repositoryScope = CoroutineScope(Job() + dispatcherProvider.io)
    private var simJob: Job? = null

    private val _simState = MutableStateFlow<SimulationState?>(null)
    val simState = _simState.asStateFlow()

    fun simulateUpToAndIncludingGame(lastGameId: Int) {
        _simState.update { SimulationState() }
        simulateGames(lastGameId)
    }

    fun simulateUpToGame(lastGameId: Int) {
        _simState.update { SimulationState(isSimulatingToGame = true) }
        simulateGames(lastGameId - 1)
    }

    fun simulateUntilConferenceTournaments() {
        _simState.update { SimulationState(isSimulatingSeason = true) }
        repositoryScope.launch {
            gameDao.getNonTournamentGames().last().id?.let {
                simulateGames(it)
            }
        }
    }

    private fun simulateGames(lastGameId: Int) {
        simJob?.cancel()
        simJob = repositoryScope.launch {
            _simState.update { it?.copy(isSimActive = true) }
            val firstGameId = gameDao.getFirstGameWithIsFinal(false)
            if (firstGameId == null || firstGameId >= lastGameId) {
                _simState.update { it?.copy(isSimActive = false) }
                return@launch
            }

            val recruits = RecruitDatabaseHelper.loadAllRecruits(database)

            _simState.update { it?.copy(numberOfGamesToSim = lastGameId - firstGameId + 1) }

            for (gameId in firstGameId..lastGameId) {
                val game = relationalDao.loadGameWithTeams(gameId).create(recruits)

                game.simulateFullGame()

                game.homeTeam.doRecruitment(game, recruits)
                game.awayTeam.doRecruitment(game, recruits)

                GameDatabaseHelper.saveGameAndStats(game, database)
                TeamDatabaseHelper.saveTeam(game.homeTeam, database)
                TeamDatabaseHelper.saveTeam(game.awayTeam, database)

                _simState.update {
                    it?.copy(numberOfGamesSimmed = it.numberOfGamesSimmed + 1)
                }

                if (!isActive) {
                    break
                }
            }
            RecruitDatabaseHelper.saveRecruits(recruits, database)
            _simState.update { it?.copy(isSimActive = false) }
        }
    }

    fun cancelSimulation() {
        simJob?.cancel("Simulation cancelled by user")
        simJob = null
    }
}
