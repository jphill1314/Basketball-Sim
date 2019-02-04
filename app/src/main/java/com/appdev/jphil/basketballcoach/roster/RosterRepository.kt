package com.appdev.jphil.basketballcoach.roster

import android.content.res.Resources
import com.appdev.jphil.basketball.BasketballFactory
import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketball.TeamFactory
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RosterRepository @Inject constructor(
    private val teamId: Int,
    private val dbHelper: DatabaseHelper,
    private val resources: Resources
): RosterContract.Repository {

    private lateinit var presenter: RosterContract.Presenter
    private var team: Team? = null

    override fun fetchData() {
        GlobalScope.launch(Dispatchers.IO) {
            if (team == null) {
                team = dbHelper.loadTeamById(teamId)
                if (team == null) {
                    createGame()
                    team = dbHelper.loadTeamById(teamId)
                }
            }
            withContext(Dispatchers.Main) { presenter.onDataFetched(team!!) }
        }
    }

    private fun createGame() {
        val teamFactory = TeamFactory(
            resources.getStringArray(R.array.first_names).asList(),
            resources.getStringArray(R.array.last_names).asList()
        )
        val conferences = BasketballFactory.setupWholeBasketballWorld(teamFactory)
        conferences.forEach {
            dbHelper.saveConference(it)
            dbHelper.saveGames(it.generateSchedule())
        }
    }

    override fun attachPresenter(presenter: RosterContract.Presenter) {
        this.presenter = presenter
    }
}