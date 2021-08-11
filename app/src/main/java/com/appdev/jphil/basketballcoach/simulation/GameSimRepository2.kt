package com.appdev.jphil.basketballcoach.simulation

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.teams.TeamRecruitInteractor
import com.appdev.jphil.basketballcoach.arch.DispatcherProvider
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.BatchInsertHelper
import com.appdev.jphil.basketballcoach.database.game.GameDao
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.database.player.PlayerDao
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import com.appdev.jphil.basketballcoach.database.team.TeamDao
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import com.flurry.sdk.id
import com.flurry.sdk.it
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameSimRepository2 @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val database: BasketballDatabase
) {

    private val repositoryScope = CoroutineScope(Job() + dispatcherProvider.io)

    fun simulateUpToAndIncludingGame(lastGameId: Int) {
        repositoryScope.launch {
            val firstGameId = GameDatabaseHelper.getFirstGameWithIsFinal(false, database)
            val recruits = RecruitDatabaseHelper.loadAllRecruits(database)

            for (gameId in firstGameId..lastGameId) {
                val game = GameDatabaseHelper.loadGameById(gameId, database)
                    ?: throw IllegalArgumentException("Cannot load game with id: $gameId")

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
            }
            RecruitDatabaseHelper.saveRecruits(recruits, database)
        }
    }

    private suspend fun loadGame(gameId: Int): Game {
        return GameDatabaseHelper.loadGameById(gameId, database)
            ?: throw IllegalArgumentException("Cannot load game with id: $gameId")
    }
}