package com.appdev.jphil.basketballcoach.standings

import com.appdev.jphil.basketballcoach.database.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StandingsRepository @Inject constructor(
    private val conferenceId: Int,
    private val dbHelper: DatabaseHelper
) : StandingsContract.Repository {

    private lateinit var presenter: StandingsContract.Presenter

    override fun fetchData() {
        GlobalScope.launch(Dispatchers.IO) {
            val conference = dbHelper.loadConferenceById(conferenceId)
            val games = dbHelper.loadCompletedGameEntities()
            withContext(Dispatchers.Main) {
                conference?.teams?.let {
                    presenter.onData(it, games)
                }
            }
        }
    }

    override fun attachPresenter(presenter: StandingsContract.Presenter) {
        this.presenter = presenter
    }
}