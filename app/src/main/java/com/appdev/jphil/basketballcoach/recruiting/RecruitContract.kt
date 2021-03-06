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
        fun onSortSelected()
        fun onPositionFilterSelected(filterType: Int)
        fun onRecruitPressed(recruit: Recruit)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        suspend fun loadRecruits(): Team
        suspend fun saveRecruits(recruits: List<Recruit>)
    }
}
