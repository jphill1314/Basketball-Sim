package com.appdev.jphil.basketballcoach.coachoverview

import com.appdev.jphil.basketballcoach.main.injection.qualifiers.CoachId
import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class CoachOverviewModule {

    @Binds
    @PerFragment
    abstract fun bindsPresenter(presenter: CoachOverviewPresenter): CoachOverviewContract.Presenter

    @Binds
    @PerFragment
    abstract fun bindsRepository(repository: CoachOverviewRepository): CoachOverviewContract.Repository

    @Module
    companion object {
        @JvmStatic
        @Provides
        @PerFragment
        @CoachId
        fun providesCoachId(fragment: CoachOverviewFragment) = fragment.args.coachId
    }
}
