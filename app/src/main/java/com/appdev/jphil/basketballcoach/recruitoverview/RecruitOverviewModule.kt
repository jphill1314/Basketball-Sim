package com.appdev.jphil.basketballcoach.recruitoverview

import com.appdev.jphil.basketballcoach.main.injection.qualifiers.RecruitId
import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class RecruitOverviewModule {

    @Binds
    @PerFragment
    abstract fun bindsPresenter(presenter: RecruitOverviewPresenter): RecruitOverviewContract.Presenter

    @Binds
    @PerFragment
    abstract fun bindsRepository(repository: RecruitOverviewRepository): RecruitOverviewContract.Repository

    @Module
    companion object {
        @PerFragment
        @RecruitId
        @JvmStatic
        @Provides
        fun providesRecruitId(fragment: RecruitOverviewFragment) = fragment.recruitId
    }
}