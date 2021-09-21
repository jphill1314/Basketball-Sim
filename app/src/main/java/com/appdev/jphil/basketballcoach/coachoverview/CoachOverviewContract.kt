package com.appdev.jphil.basketballcoach.coachoverview

import com.appdev.jphil.basketballcoach.MVPContract
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity

interface CoachOverviewContract {

    interface View : MVPContract.View {
        fun displayCoach(coach: CoachEntity)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchData()
    }

    interface Repository : MVPContract.Repository<Presenter> {
        suspend fun loadCoach(): CoachEntity
    }
}
