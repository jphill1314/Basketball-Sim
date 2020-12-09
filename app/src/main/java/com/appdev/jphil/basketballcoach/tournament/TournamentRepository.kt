package com.appdev.jphil.basketballcoach.tournament

import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.conference.ConferenceDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.ConferenceId
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
            ConferenceDatabaseHelper.loadConferenceById(conferenceId, database)?.let {
                ConferenceDatabaseHelper.createTournament(it, database)?.let { tournament ->
                    withContext(Dispatchers.Main) {
                        presenter.onTournamentLoaded(tournament.getScheduleDataModels())
                    }
                    if (tournament.getWinnerOfTournament() != null) {
                        ConferenceDatabaseHelper.saveOnlyConferences(listOf(it), database)
                    }
                }
            }
            ConferenceDatabaseHelper.loadAllConferencesExcept(conferenceId, database).forEach {
                if (it.tournament?.getWinnerOfTournament() != null) {
                    ConferenceDatabaseHelper.saveOnlyConferences(listOf(it), database)
                }
            }
            withContext(Dispatchers.Main) {
                presenter.onTournamentSetupComplete()
            }
        }
    }

    override fun attachPresenter(presenter: TournamentContract.Presenter) {
        this.presenter = presenter
    }
}
