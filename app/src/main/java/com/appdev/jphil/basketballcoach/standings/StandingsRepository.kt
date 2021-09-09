package com.appdev.jphil.basketballcoach.standings

import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameDao
import com.appdev.jphil.basketballcoach.database.relations.RelationalDao
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.ConferenceId
import javax.inject.Inject

class StandingsRepository @Inject constructor(
    @ConferenceId private var conferenceId: Int,
    private val database: BasketballDatabase,
    private val relationalDao: RelationalDao,
    private val gameDao: GameDao
) : StandingsContract.Repository {

    private lateinit var presenter: StandingsContract.Presenter

    override suspend fun fetchData(): StandingsModel {
        val conferences = ConferenceDatabaseHelper.loadAllConferenceEntities(database)
        val games = gameDao.getGamesWithIsFinal(true)
        val recruits = relationalDao.loadAllRecruits().map { it.create() }
        val conference = relationalDao.loadConferenceDataById(conferenceId).createConference(recruits)
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
