package com.appdev.jphil.basketballcoach.recruiting

import com.appdev.jphil.basketball.recruits.Recruit
import javax.inject.Inject

class RecruitPresenter @Inject constructor(
    private val repository: RecruitContract.Repository
) : RecruitContract.Presenter {

    private var view : RecruitContract.View? = null

    init {
        repository.attachPresenter(this)
    }

    override fun fetchData() {
        repository.loadRecruits()
    }

    override fun onRecruitsLoaded(recruits: MutableList<Recruit>) {
        view?.displayRecruits(recruits)
    }

    override fun onViewAttached(view: RecruitContract.View) {
        this.view = view
    }

    override fun onViewDetached() {
        view = null
    }
}