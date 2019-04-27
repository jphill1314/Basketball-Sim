package com.appdev.jphil.basketballcoach.coaches

import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class CoachesModule {

    @Binds
    @PerFragment
    abstract fun bindsPresenter(presenter: CoachesPresenter): CoachesContract.Presenter

    @Binds
    @PerFragment
    abstract fun bindsRepository(repository: CoachesRepository): CoachesContract.Repository

    @Module
    companion object {
        @Provides
        @PerFragment
        @JvmStatic
        fun providesTeamId(fragment: CoachesFragment) = fragment.teamId
    }
}