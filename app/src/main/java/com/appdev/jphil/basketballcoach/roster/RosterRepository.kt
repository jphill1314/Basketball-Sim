package com.appdev.jphil.basketballcoach.roster

import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketballcoach.database.DatabaseHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class RosterRepository @Inject constructor(private val dbHelper: DatabaseHelper): RosterContract.Repository {

    private lateinit var presenter: RosterContract.Presenter
    private var team: Team? = null

    override fun fetchData() {
        GlobalScope.launch {
            if (team == null) {
                team = dbHelper.loadTeamById(1)
                if (team == null) {
                    team = Team(1, "My Greatest Team", 70, true)
                    dbHelper.saveTeam(team!!)
                }
            }
            presenter.onDataFetched(team!!)
        }
    }

    override fun attachPresenter(presenter: RosterContract.Presenter) {
        this.presenter = presenter
    }
}