package com.appdev.jphil.basketballcoach.roster

import android.content.res.Resources
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.relations.RelationalDao
import com.appdev.jphil.basketballcoach.database.team.TeamDatabaseHelper
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import com.appdev.jphil.basketballcoach.newseason.NewGameGenerator
import dagger.Lazy
import javax.inject.Inject

class RosterRepository @Inject constructor(
    @TeamId private val lazyTeamId: Lazy<Int>,
    private val database: BasketballDatabase,
    private val relationalDao: RelationalDao,
    private val resources: Resources
) : RosterContract.Repository {

    private lateinit var presenter: RosterContract.Presenter
    private val recruits = mutableListOf<Recruit>()

    override suspend fun fetchData(): Team {
        val teamId = lazyTeamId.get()
        if (recruits.isEmpty()) {
            recruits.addAll(relationalDao.loadAllRecruits().map { it.create() })
        }

        var team = if (teamId == -1) {
            relationalDao.loadUserTeamNullable()
        } else {
            relationalDao.loadTeamById(teamId)
        }

        if (team == null) {
            NewGameGenerator.generateNewGame(resources, database)
            recruits.clear()
            recruits.addAll(relationalDao.loadAllRecruits().map { it.create() })
            team = relationalDao.loadUserTeam()
        }
        return team.create(recruits)
    }

    override suspend fun saveTeam(team: Team) {
        TeamDatabaseHelper.saveTeam(team, database)
    }

    override fun attachPresenter(presenter: RosterContract.Presenter) {
        this.presenter = presenter
    }
}
