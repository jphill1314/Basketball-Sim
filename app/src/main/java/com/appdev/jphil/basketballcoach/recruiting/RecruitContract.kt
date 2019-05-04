package com.appdev.jphil.basketballcoach.recruiting

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketballcoach.MVPContract

interface RecruitContract {

    interface View : MVPContract.View {
        fun displayRecruits(recruits: List<Recruit>)
        fun updateRecruits(recruits: List<Recruit>)
        fun displayRecruitDialog(recruit: Recruit)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
        fun onRecruitsLoaded(recruits: MutableList<Recruit>, team: Team)
        fun onSortSelected()
        fun onPositionFilterSelected(filterType: Int)
        fun onInteractionFilterSelected(filterType: Int)
        fun onRecruitLongPressed(recruit: Recruit)
        fun interactWithRecruit(recruit: Recruit, interaction: Int)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun loadRecruits()
        fun saveRecruits(recruits: List<Recruit>)
    }
}