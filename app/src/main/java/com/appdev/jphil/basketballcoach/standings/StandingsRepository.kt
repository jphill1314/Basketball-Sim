package com.appdev.jphil.basketballcoach.standings

import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDatabaseHelper
import com.appdev.jphil.basketballcoach.database.game.GameDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.ConferenceId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StandingsRepository @Inject constructor(
    @ConferenceId private var conferenceId: Int,
    private val database: BasketballDatabase
) : StandingsContract.Repository {

    private lateinit var presenter: StandingsContract.Presenter

    override fun fetchData() {
        GlobalScope.launch(Dispatchers.IO) {
            val conferences = ConferenceDatabaseHelper.loadAllConferenceEntities(database)
            val conference = ConferenceDatabaseHelper.loadConferenceById(conferenceId, database)
            val games = GameDatabaseHelper.loadCompletedGameEntities(database)
            withContext(Dispatchers.Main) {
                conference?.teams?.let {
                    presenter.onData(it, games, conferences)
                }
            }
        }
    }

    override fun onConferenceIdChanged(confId: Int) {
        conferenceId = confId
        fetchData()
    }

    override fun attachPresenter(presenter: StandingsContract.Presenter) {
        this.presenter = presenter
    }
}