package com.appdev.jphil.basketballcoach.recruitoverview

import com.appdev.jphil.basketball.recruits.Recruit
import javax.inject.Inject

class RecruitOverviewPresenter @Inject constructor(
    private val repository: RecruitOverviewContract.Repository
) : RecruitOverviewContract.Presenter{

    private var view: RecruitOverviewContract.View? = null

    init {
        repository.attachPresenter(this)
    }

    override fun fetchData() {
        repository.loadRecruit()
    }

    override fun onRecruitLoaded(recruit: Recruit) {
        view?.displayRecruit(recruit)
    }

    override fun onViewAttached(view: RecruitOverviewContract.View) {
        this.view = view
    }

    override fun onViewDetached() {
        view = null
    }
}