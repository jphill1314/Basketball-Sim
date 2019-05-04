package com.appdev.jphil.basketballcoach.recruitoverview

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.MVPContract

interface RecruitOverviewContract {

    interface View : MVPContract.View {
        fun displayRecruit(recruit: Recruit)
        fun disableAllButtons()
        fun setEnableForScholarshipButton(isEnabled: Boolean)
        fun setEnableForVisitButton(isEnabled: Boolean)
        fun showToast(message: String)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
        fun onRecruitLoaded(recruit: Recruit, team: Team)
        fun onScoutClicked()
        fun onContactClicked()
        fun onOfferScholarshipClicked()
        fun onOfficialVisitClicked()
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun loadRecruit()
        fun saveRecruit(recruit: Recruit)
    }
}