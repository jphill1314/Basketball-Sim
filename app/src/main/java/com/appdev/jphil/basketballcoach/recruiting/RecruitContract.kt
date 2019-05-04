package com.appdev.jphil.basketballcoach.recruiting

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketballcoach.MVPContract

interface RecruitContract {

    interface View : MVPContract.View {
        fun displayRecruits(recruits: List<Recruit>, team: Team)
        fun updateRecruits(recruits: List<Recruit>)
        fun goToRecruitOverview(recruitId: Int)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
        fun onRecruitsLoaded(recruits: MutableList<Recruit>, team: Team)
        fun onSortSelected()
        fun onPositionFilterSelected(filterType: Int)
        fun onInteractionFilterSelected(filterType: Int)
        fun onRecruitPressed(recruit: Recruit)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun loadRecruits()
        fun saveRecruits(recruits: List<Recruit>)
    }
}