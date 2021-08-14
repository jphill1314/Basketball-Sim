package com.appdev.jphil.basketballcoach.recruitoverview

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.recruits.RecruitInterest
import com.appdev.jphil.basketball.recruits.RecruitingEvent
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.teams.TeamRecruitInteractor
import com.appdev.jphil.basketballcoach.arch.BasePresenter
import com.appdev.jphil.basketballcoach.arch.DispatcherProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecruitOverviewPresenter @Inject constructor(
    private val repository: RecruitOverviewContract.Repository,
    dispatcherProvider: DispatcherProvider
) : BasePresenter(dispatcherProvider), RecruitOverviewContract.Presenter {

    private var view: RecruitOverviewContract.View? = null
    private lateinit var recruit: Recruit
    private lateinit var interest: RecruitInterest
    private lateinit var team: Team

    init {
        repository.attachPresenter(this)
    }

    override fun fetchData() {
        coroutineScope.launch {
            repository.loadRecruit().let {
                recruit = it.recruit
                team = it.team
            }
            interest = recruit.interestInTeams.first { it.teamId == team.teamId }
            view?.displayRecruit(recruit)
            if (!team.isUser) {
                view?.disableAllButtons()
            }
        }
    }

    override fun onScoutClicked() {
        if (doInteraction()) {
            recruit.updateInterest(team, RecruitingEvent.SCOUT, team.gamesPlayed)
            view?.displayRecruit(recruit)
        } else {
            // TODO: use resources for this
            view?.showToast("You've done this too recently to do again")
        }
    }

    override fun onOfferScholarshipClicked() {
        if (doInteraction() && !interest.isOfferedScholarship) {
            recruit.updateInterest(team, RecruitingEvent.OFFER_SCHOLARSHIP, team.gamesPlayed)
            view?.displayRecruit(recruit)
        } else {
            view?.showToast("You can only do this action once")
        }
    }

    private fun doInteraction(): Boolean {
        return interest.lastInteractionGame + TeamRecruitInteractor.GAMES_BETWEEN_INTERACTIONS <= team.gamesPlayed
    }

    override fun onViewAttached(view: RecruitOverviewContract.View) {
        this.view = view
    }

    override fun onViewDetached() {
        view = null
        coroutineScope.launch {
            repository.saveRecruit(recruit)
        }
    }
}
