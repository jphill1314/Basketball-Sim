package com.appdev.jphil.basketballcoach.tournament

import com.appdev.jphil.basketball.StandingsDataModel
import com.appdev.jphil.basketball.TenTeamTournament
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.util.RecordUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TournamentRepository @Inject constructor(
    private val database: BasketballDatabase,
    private val conferenceId: Int
): TournamentContract.Repository {

    private lateinit var presenter: TournamentContract.Presenter

    override fun fetchData() {
        GlobalScope.launch(Dispatchers.IO) {
            createTournament()?.let { tournament ->
                val games = GameDatabaseHelper.loadAllGameEntities(database)
                withContext(Dispatchers.Main) {
                    presenter.onTournamentLoaded(tournament, games)
                }
            }
        }
    }

    override fun simToGame() {
        GlobalScope.launch(Dispatchers.IO) {
            var currentGameId = GameDatabaseHelper.loadAllGameEntities(database).sortedBy { it.id }.first().id ?: 1
            var homeName = ""
            var awayName = ""
            var userIsHomeTeam = false
            var continueSim = true
            var gameLoaded = false
            while(continueSim) {
                val game = GameDatabaseHelper.loadGameById(currentGameId++, database)
                if (game == null) {
                    continueSim = false
                }
                else {
                    if (!game.isFinal) {
                        continueSim = !(game.homeTeam.isUser || game.awayTeam.isUser)
                        if (continueSim) {
                            game.simulateFullGame()
                            GameDatabaseHelper.saveGames(listOf(game), database)
                        } else {
                            gameLoaded = true
                            homeName = game.homeTeam.name
                            awayName = game.awayTeam.name
                            userIsHomeTeam = game.homeTeam.isUser
                        }
                    }
                }
            }
            if (gameLoaded) {
                withContext(Dispatchers.Main) {
                    presenter.startGameFragment(currentGameId - 1, homeName, awayName, userIsHomeTeam)
                }
            } else {
                generateNextRound()
                fetchData()
            }
        }
    }

    private fun generateNextRound() {
        createTournament()?.let {
            it.generateNextRound(2018) // TODO: inject current season
            GameDatabaseHelper.saveOnlyGames(it.games, database)
            it.games.clear()
            it.games.addAll(GameDatabaseHelper.loadGamesForTournament(it.id, database))
        }
    }

    private fun createTournament(): TenTeamTournament? {
        val conference = ConferenceDatabaseHelper.loadConferenceById(conferenceId, database)
        val games = GameDatabaseHelper.loadAllGameEntities(database)
        val standings = mutableListOf<StandingsDataModel>()
        conference?.teams?.forEach { team -> standings.add(RecordUtil.getRecordAsPair(games, team)) }

        conference?.generateTournament(standings.sortedWith(compareBy(
            { -it.getConferenceWinPercentage() },
            { -it.conferenceWins },
            { -it.getWinPercentage() },
            { -it.totalWins }
        )))

        conference?.tournament?.let { tournament ->
            var tournamentGames = GameDatabaseHelper.loadGamesForTournament(conferenceId, database)
            tournament.games.addAll(tournamentGames)
            if (tournament.games.size == 0) {
                tournament.generateNextRound(2018) // TODO: inject
                GameDatabaseHelper.saveOnlyGames(tournament.games, database)
                tournamentGames = GameDatabaseHelper.loadGamesForTournament(conferenceId, database)
                tournament.games.clear()
                tournament.games.addAll(tournamentGames)
            }
        }
        return conference?.tournament
    }

    override fun attachPresenter(presenter: TournamentContract.Presenter) {
        this.presenter = presenter
    }
}