package com.appdev.jphil.basketballcoach.recruitoverview

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketballcoach.MVPContract

interface RecruitOverviewContract {

    interface View : MVPContract.View {
        fun displayRecruit(recruit: Recruit)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
        fun onRecruitLoaded(recruit: Recruit)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun loadRecruit()
    }
}