package com.appdev.jphil.basketballcoach.roster

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.tracking.TrackingKeys
import com.flurry.android.FlurryAgent
import javax.inject.Inject

class RosterPresenter @Inject constructor(private val repository: RosterContract.Repository): RosterContract.Presenter {

    private var view: RosterContract.View? = null
    private lateinit var team: Team
    private var player: Player? = null

    init {
        repository.attachPresenter(this)
    }

    override fun fetchData() {
        repository.fetchData()
    }

    override fun onDataFetched(team: Team) {
        this.team = team
        view?.updateTeamAndConference(team.teamId, team.conferenceId)
        displayData(team.roster, team)
    }

    override fun onPlayerSelected(player: Player) {
        if (this.player == null) {
            this.player = player
        } else {
            if (this.player?.id != player.id) {
                team.swapPlayers(player.rosterIndex, this.player!!.rosterIndex)
                repository.saveTeam(team)
                displayData(team.roster, team)
                FlurryAgent.logEvent(TrackingKeys.EVENT_TAP, mapOf(TrackingKeys.PAYLOAD_TAP_TYPE to TrackingKeys.VALUE_SWAP_PLAYERS))
            }
            this.player = null
        }
    }

    override fun onPlayerLongPressed(player: Player) {
        view?.gotoPlayerOverview(player.id!!)
    }

    private fun displayData(roster: List<Player>, team: Team) {
        val dataModels = mutableListOf<RosterDataModel>()
        roster.forEach { dataModels.add(RosterDataModel(it, false)) }
        view?.displayData(dataModels, team)
    }

    override fun onViewAttached(view: RosterContract.View) {
        this.view = view
    }

    override fun onViewDetached() {
        view = null
    }
}