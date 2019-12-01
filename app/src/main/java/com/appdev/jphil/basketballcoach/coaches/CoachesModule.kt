package com.appdev.jphil.basketballcoach.coaches

import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import dagger.Binds
import dagger.Module

@Module
abstract class CoachesModule {

    @Binds
    @PerFragment
    abstract fun bindsPresenter(presenter: CoachesPresenter): CoachesContract.Presenter

    @Binds
    @PerFragment
    abstract fun bindsRepository(repository: CoachesRepository): CoachesContract.Repository
}