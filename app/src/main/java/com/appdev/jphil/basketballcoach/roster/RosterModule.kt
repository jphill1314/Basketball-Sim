package com.appdev.jphil.basketballcoach.roster

import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class RosterModule {

    @Binds
    @PerFragment
    abstract fun providesPresenter(presenter: RosterPresenter): RosterContract.Presenter

    @Binds
    @PerFragment
    abstract fun providesRepository(repository: RosterRepository): RosterContract.Repository
}