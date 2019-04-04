package com.appdev.jphil.basketballcoach.tournament

import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class TournamentModule {

    @Binds
    @PerFragment
    abstract fun bindPresenter(presenter: TournamentPresenter): TournamentContract.Presenter

    @Binds
    @PerFragment
    abstract fun bindRepository(repository: TournamentRepository): TournamentContract.Repository

    @Module
    companion object {
        @JvmStatic
        @Provides
        @PerFragment
        fun providesConferenceId(fragment: TournamentFragment) = fragment.confId
    }
}