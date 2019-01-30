package com.appdev.jphil.basketballcoach.schedule

import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import dagger.Binds
import dagger.Module

@Module
abstract class ScheduleModule {

    @Binds
    @PerFragment
    abstract fun providesPresenter(presenter: SchedulePresenter): ScheduleContract.Presenter

    @Binds
    @PerFragment
    abstract fun providesRepository(repository: ScheduleRepository): ScheduleContract.Repository
}