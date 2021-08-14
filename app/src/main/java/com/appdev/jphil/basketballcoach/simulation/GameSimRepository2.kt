package com.appdev.jphil.basketballcoach.simulation

import com.appdev.jphil.basketball.teams.TeamRecruitInteractor
import com.appdev.jphil.basketballcoach.arch.DispatcherProvider
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
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

class GameSimRepository2 @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val database: BasketballDatabase,
    private val relationalDao: RelationalDao
) {

    data class SimulationState(
        val isSimActive: Boolean = false,
        val isSimulatingToGame: Boolean = false,
        val numberOfGamesToSim: Int = 0,
        val numberOfGamesSimmed: Int = 0
    )

    private val repositoryScope = CoroutineScope(Job() + dispatcherProvider.io)
    private var simJob: Job? = null

    private val _isSimulationActive = MutableStateFlow<SimulationState?>(null)
    val isSimulationActive = _isSimulationActive.asStateFlow()

    fun simulateUpToAndIncludingGame(lastGameId: Int) {
        simJob?.cancel()
        _isSimulationActive.update { SimulationState() }
        simJob = repositoryScope.launch {
            _isSimulationActive.update { it?.copy(isSimActive = true) }
            val firstGameId = GameDatabaseHelper.getFirstGameWithIsFinal(false, database)
            val recruits = RecruitDatabaseHelper.loadAllRecruits(database)

            _isSimulationActive.update { it?.copy(numberOfGamesToSim = lastGameId - firstGameId + 1) }

            for (gameId in firstGameId..lastGameId) {
                val game = GameDatabaseHelper.getGameById(gameId, recruits, relationalDao)

                game.simulateFullGame()
                recruits.forEach { it.updateInterestAfterGame(game) }

                game.homeTeam.doScouting(recruits)
                game.awayTeam.doScouting(recruits)

                if (!game.homeTeam.isUser) {
                    TeamRecruitInteractor.interactWithRecruits(game.homeTeam, recruits)
                }
                if (!game.awayTeam.isUser) {
                    TeamRecruitInteractor.interactWithRecruits(game.awayTeam, recruits)
                }

                GameDatabaseHelper.saveGameAndStats(game, database)
                TeamDatabaseHelper.saveTeam(game.homeTeam, database)
                TeamDatabaseHelper.saveTeam(game.awayTeam, database)

                _isSimulationActive.update {
                    it?.copy(numberOfGamesSimmed = it.numberOfGamesSimmed + 1)
                }

                if (!isActive) {
                    break
                }
            }
            RecruitDatabaseHelper.saveRecruits(recruits, database)
            _isSimulationActive.update { it?.copy(isSimActive = false) }
        }
    }

    fun simulateUpToGame(lastGameId: Int) {
        simJob?.cancel()
        _isSimulationActive.update { SimulationState(isSimulatingToGame = true) }
        simJob = repositoryScope.launch {
            _isSimulationActive.update { it?.copy(isSimActive = true) }
            val firstGameId = GameDatabaseHelper.getFirstGameWithIsFinal(false, database)
            val recruits = RecruitDatabaseHelper.loadAllRecruits(database)

            _isSimulationActive.update { it?.copy(numberOfGamesToSim = lastGameId - firstGameId + 1) }

            for (gameId in firstGameId until lastGameId) {
                val game = GameDatabaseHelper.getGameById(gameId, recruits, relationalDao)

                game.simulateFullGame()
                recruits.forEach { it.updateInterestAfterGame(game) }

                game.homeTeam.doScouting(recruits)
                game.awayTeam.doScouting(recruits)

                if (!game.homeTeam.isUser) {
                    TeamRecruitInteractor.interactWithRecruits(game.homeTeam, recruits)
                }
                if (!game.awayTeam.isUser) {
                    TeamRecruitInteractor.interactWithRecruits(game.awayTeam, recruits)
                }

                GameDatabaseHelper.saveGameAndStats(game, database)
                TeamDatabaseHelper.saveTeam(game.homeTeam, database)
                TeamDatabaseHelper.saveTeam(game.awayTeam, database)

                _isSimulationActive.update {
                    it?.copy(numberOfGamesSimmed = it.numberOfGamesSimmed + 1)
                }

                if (!isActive) {
                    break
                }
            }
            RecruitDatabaseHelper.saveRecruits(recruits, database)
            _isSimulationActive.update { it?.copy(isSimActive = false) }
        }
    }

    fun cancelSimulation() {
        simJob?.cancel("Simulation cancelled by user")
        simJob = null
    }
}