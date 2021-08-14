package com.appdev.jphil.basketballcoach.standings

import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.ConferenceId
import javax.inject.Inject

class StandingsRepository @Inject constructor(
    @ConferenceId private var conferenceId: Int,
    private val database: BasketballDatabase
) : StandingsContract.Repository {

    private lateinit var presenter: StandingsContract.Presenter

    override suspend fun fetchData(): StandingsModel {
        val conferences = ConferenceDatabaseHelper.loadAllConferenceEntities(database)
        val games = GameDatabaseHelper.loadCompletedGameEntities(database)
        val conference = ConferenceDatabaseHelper.loadConferenceById(conferenceId, database)
            ?: throw IllegalStateException("No conference exists for conferenceId = $conferenceId")
        return StandingsModel(conference.teams, games, conferences)
    }

    override suspend fun onConferenceIdChanged(confId: Int): StandingsModel {
        conferenceId = confId
        return fetchData()
    }

    override fun attachPresenter(presenter: StandingsContract.Presenter) {
        this.presenter = presenter
    }
}
