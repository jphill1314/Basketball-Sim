package com.appdev.jphil.basketballcoach.recruiting

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketballcoach.MVPContract

interface RecruitContract {

    interface View : MVPContract.View {
        fun displayRecruits(recruits: MutableList<Recruit>)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
        fun onRecruitsLoaded(recruits: MutableList<Recruit>)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun loadRecruits()
    }
}