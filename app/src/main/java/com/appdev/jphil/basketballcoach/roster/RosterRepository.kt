package com.appdev.jphil.basketballcoach.roster

import android.content.res.Resources
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.player.PlayerDao
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity
import com.appdev.jphil.basketballcoach.database.relations.RelationalDao
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import com.appdev.jphil.basketballcoach.newseason.NewGameGenerator
import dagger.Lazy
import javax.inject.Inject

class RosterRepository @Inject constructor(
    @TeamId private val lazyTeamId: Lazy<Int>,
    private val database: BasketballDatabase,
    private val relationalDao: RelationalDao,
    private val playerDao: PlayerDao,
    private val resources: Resources
) : RosterContract.Repository {

    private lateinit var presenter: RosterContract.Presenter

    override suspend fun fetchData(): Team {
        val teamId = lazyTeamId.get()

        var team = if (teamId == -1) {
            relationalDao.loadUserTeamNullable()
        } else {
            relationalDao.loadTeamById(teamId)
        }

        if (team == null) {
            NewGameGenerator.generateNewGame(resources, database)
            team = relationalDao.loadUserTeam()
        }
        return team.create(emptyList())
    }

    override suspend fun saveTeam(team: Team) {
        playerDao.insertPlayers(team.players.map { PlayerEntity.from(it) })
    }

    override fun attachPresenter(presenter: RosterContract.Presenter) {
        this.presenter = presenter
    }
}
