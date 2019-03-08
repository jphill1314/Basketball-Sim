package com.appdev.jphil.basketballcoach.standings

import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class StandingsModule {

    @Binds
    @PerFragment
    abstract fun bindsPresenter(presenter: StandingsPresenter): StandingsContract.Presenter

    @Binds
    @PerFragment
    abstract fun bindsRepository(repository: StandingsRepository): StandingsContract.Repository

    @Module
    companion object {
        @JvmStatic
        @PerFragment
        @Provides
        fun providesConferenceId(fragment: StandingsFragment) = fragment.conferenceId
    }
}