package com.appdev.jphil.basketballcoach.tournament

import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketball.tournament.TenTeamTournament
import com.appdev.jphil.basketball.tournament.Tournament
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.ConferenceId
import com.appdev.jphil.basketballcoach.util.RecordUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TournamentRepository @Inject constructor(
    private val database: BasketballDatabase,
    @ConferenceId private val conferenceId: Int
): TournamentContract.Repository {

    private lateinit var presenter: TournamentContract.Presenter

    override fun fetchData() {
        GlobalScope.launch(Dispatchers.IO) {
            createTournament()?.let { tournament ->
                if (GameDatabaseHelper.loadAllGameEntities(database).filter { !it.isFinal }.isEmpty()) {
                    generateNextRound(tournament)
                }
                withContext(Dispatchers.Main) {
                    presenter.onTournamentLoaded(tournament.getScheduleDataModels())
                }
            }
        }
    }

    private fun generateNextRound(tournament: Tournament) {
        tournament.generateNextRound(2018) // TODO: inject current season
        GameDatabaseHelper.saveOnlyGames(tournament.getAllGames(), database)
        tournament.replaceGames(GameDatabaseHelper.loadGamesForTournament(tournament.getId(), database))
    }

    private fun createTournament(): Tournament? {
        val conference = ConferenceDatabaseHelper.loadConferenceById(conferenceId, database)
        val games = GameDatabaseHelper.loadAllGameEntities(database)
        val standings = mutableListOf<StandingsDataModel>()
        conference?.teams?.forEach { team -> standings.add(RecordUtil.getRecord(games, team)) }

        conference?.generateTournament(standings.sortedWith(compareBy(
            { -it.getConferenceWinPercentage() },
            { -it.conferenceWins },
            { -it.getWinPercentage() },
            { -it.totalWins }
        )))

        conference?.tournament?.let { tournament ->
            tournament.replaceGames(GameDatabaseHelper.loadGamesForTournament(conferenceId, database))
            if (tournament.numberOfGames() == 0) {
                generateNextRound(tournament)
            }
        }
        return conference?.tournament
    }

    override fun attachPresenter(presenter: TournamentContract.Presenter) {
        this.presenter = presenter
    }
}