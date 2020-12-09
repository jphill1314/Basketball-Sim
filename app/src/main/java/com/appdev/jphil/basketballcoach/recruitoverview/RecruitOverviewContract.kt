package com.appdev.jphil.basketballcoach.recruitoverview

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.MVPContract

interface RecruitOverviewContract {

    interface View : MVPContract.View {
        fun displayRecruit(recruit: Recruit)
        fun disableAllButtons()
        fun showToast(message: String)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
        fun onScoutClicked()
        fun onOfferScholarshipClicked()
    }

    interface Repository : MVPContract.Repository<Presenter> {
        suspend fun loadRecruit(): RecruitOverviewModel
        suspend fun saveRecruit(recruit: Recruit)
    }
}
