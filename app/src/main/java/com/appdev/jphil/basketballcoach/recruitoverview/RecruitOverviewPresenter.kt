package com.appdev.jphil.basketballcoach.recruitoverview

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.recruits.RecruitInterest
import com.appdev.jphil.basketball.recruits.RecruitingEvent
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.teams.TeamRecruitInteractor
import javax.inject.Inject

class RecruitOverviewPresenter @Inject constructor(
    private val repository: RecruitOverviewContract.Repository
) : RecruitOverviewContract.Presenter{

    private var view: RecruitOverviewContract.View? = null
    private lateinit var recruit: Recruit
    private lateinit var interest: RecruitInterest
    private lateinit var team: Team

    init {
        repository.attachPresenter(this)
    }

    override fun fetchData() {
        repository.loadRecruit()
    }

    override fun onRecruitLoaded(recruit: Recruit, team: Team) {
        this.recruit = recruit
        this.team = team
        interest = recruit.interestInTeams.filter { it.teamId == team.teamId }[0]
        view?.displayRecruit(recruit)
        if (!team.isUser) {
            view?.disableAllButtons()
        } else {
            view?.setEnableForScholarshipButton(interest.isScouted && interest.isContacted && !interest.isOfferedScholarship)
            view?.setEnableForVisitButton(interest.isScouted && interest.isContacted && interest.isOfferedScholarship && !interest.isOfficialVisitDone)
        }
    }

    override fun onScoutClicked() {
        if (doInteraction()){
            recruit.updateInterest(team, RecruitingEvent.SCOUT, team.gamesPlayed)
            view?.displayRecruit(recruit)
        } else {
            // TODO: use resources for this
            view?.showToast("You've done this too recently to do again")
        }
    }

    override fun onContactClicked() {
        if (doInteraction()){
            recruit.updateInterest(team, RecruitingEvent.COACH_CONTACT, team.gamesPlayed)
            view?.displayRecruit(recruit)
        } else {
            view?.showToast("You've done this too recently to do again")
        }
    }

    override fun onOfferScholarshipClicked() {
        if (doInteraction() && !interest.isOfferedScholarship){
            recruit.updateInterest(team, RecruitingEvent.OFFER_SCHOLARSHIP, team.gamesPlayed)
            view?.displayRecruit(recruit)
        } else {
            view?.showToast("You can only do this action once")
        }
    }

    override fun onOfficialVisitClicked() {
        if (doInteraction() && !interest.isOfficialVisitDone){
            recruit.updateInterest(team, RecruitingEvent.OFFICIAL_VISIT, team.gamesPlayed)
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
        repository.saveRecruit(recruit)
        view = null
    }
}