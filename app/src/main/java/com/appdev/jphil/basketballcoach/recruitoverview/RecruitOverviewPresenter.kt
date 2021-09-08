package com.appdev.jphil.basketballcoach.recruitoverview

import com.appdev.jphil.basketball.recruits.NewRecruitInterest
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team
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
    private lateinit var interest: NewRecruitInterest
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
            interest = recruit.recruitInterests.first { it.teamId == team.teamId }
            view?.displayRecruit(recruit)
            if (!team.isUser) {
                view?.disableAllButtons()
            }
        }
    }

    override fun onScoutClicked() {

    }

    override fun onOfferScholarshipClicked() {

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
