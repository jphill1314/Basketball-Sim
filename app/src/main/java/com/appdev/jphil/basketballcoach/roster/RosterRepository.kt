package com.appdev.jphil.basketballcoach.roster

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.player.PlayerDao
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity
import com.appdev.jphil.basketballcoach.database.relations.RelationalDao
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import javax.inject.Inject

class RosterRepository @Inject constructor(
    @TeamId private val teamId: Int,
    private val relationalDao: RelationalDao,
    private val playerDao: PlayerDao
) : RosterContract.Repository {

    private lateinit var presenter: RosterContract.Presenter

    override suspend fun fetchData(): Team {
        return relationalDao.loadTeamById(teamId).create(emptyList())
    }

    override suspend fun saveTeam(team: Team) {
        playerDao.insertPlayers(team.players.map { PlayerEntity.from(it) })
    }

    override fun attachPresenter(presenter: RosterContract.Presenter) {
        this.presenter = presenter
    }
}
