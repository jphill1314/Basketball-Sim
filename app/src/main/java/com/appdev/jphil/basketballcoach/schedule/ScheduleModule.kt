package com.appdev.jphil.basketballcoach.schedule

import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import com.appdev.jphil.basketballcoach.simulation.GameSimRepository
import com.appdev.jphil.basketballcoach.simulation.SimulationContract
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class ScheduleModule {

    @Binds
    @PerFragment
    abstract fun providesPresenter(presenter: SchedulePresenter): ScheduleContract.Presenter

    @Binds
    @PerFragment
    abstract fun providesRepository(repository: ScheduleRepository): ScheduleContract.Repository

    @Binds
    @PerFragment
    abstract fun providesGameSimRepository(repository: GameSimRepository): SimulationContract.GameSimRepository
}