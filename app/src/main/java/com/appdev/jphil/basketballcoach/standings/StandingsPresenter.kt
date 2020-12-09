package com.appdev.jphil.basketballcoach.standings

import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.arch.BasePresenter
import com.appdev.jphil.basketballcoach.arch.DispatcherProvider
import com.appdev.jphil.basketballcoach.database.conference.ConferenceEntity
import com.appdev.jphil.basketballcoach.database.game.GameEntity
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.ConferenceId
import com.appdev.jphil.basketballcoach.tracking.TrackingKeys
import com.appdev.jphil.basketballcoach.util.RecordUtil
import com.flurry.android.FlurryAgent
import kotlinx.coroutines.launch
import javax.inject.Inject

class StandingsPresenter @Inject constructor(
    @ConferenceId private var conferenceId: Int,
    private val repository: StandingsContract.Repository,
    dispatcherProvider: DispatcherProvider
) : BasePresenter(dispatcherProvider), StandingsContract.Presenter {

    init {
        repository.attachPresenter(this)
    }

    private var view: StandingsContract.View? = null
    private val standings = mutableListOf<StandingsDataModel>()

    override fun fetchData() {
        if (standings.isEmpty()) {
            coroutineScope.launch {
                repository.fetchData().run { onData(teams, games, conferences) }
            }
        } else {
            view?.addTeams(standings)
        }
    }

    override fun onTeamSelected(standingsDataModel: StandingsDataModel) {
        view?.changeTeamAndConference(standingsDataModel)
        FlurryAgent.logEvent(TrackingKeys.EVENT_TAP, mapOf(TrackingKeys.PAYLOAD_TAP_TYPE to TrackingKeys.VALUE_SELECT_TEAM))
    }

    override fun onConferenceChanged(confId: Int) {
        if (confId != conferenceId) {
            conferenceId = confId

            coroutineScope.launch {
                repository.onConferenceIdChanged(confId).run { onData(teams, games, conferences) }
            }
        }
    }

    private fun onData(teams: List<Team>, games: List<GameEntity>, conferences: List<ConferenceEntity>) {
        val names = mutableListOf<String>()
        conferences.forEach { names.add(it.name) }
        view?.addConferenceNames(names)
        standings.clear()

        teams.forEach { team ->
            standings.add(RecordUtil.getRecord(games, team))
        }

        view?.addTeams(standings.sortedWith(compareBy(
            { -it.getConferenceWinPercentage() },
            { -it.conferenceWins },
            { -it.getWinPercentage() },
            { -it.totalWins }
        )))
    }

    override fun onViewAttached(view: StandingsContract.View) {
        this.view = view
    }

    override fun onViewDetached() {
        view = null
    }
}
