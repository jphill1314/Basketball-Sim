package com.appdev.jphil.basketballcoach.tournament

import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import com.appdev.jphil.basketballcoach.simulation.GameSimRepository
import com.appdev.jphil.basketballcoach.simulation.SimulationContract
import dagger.Binds
import dagger.Module

@Module
abstract class TournamentModule {

    @Binds
    @PerFragment
    abstract fun bindPresenter(presenter: TournamentPresenter): TournamentContract.Presenter

    @Binds
    @PerFragment
    abstract fun bindRepository(repository: TournamentRepository): TournamentContract.Repository

    @Binds
    @PerFragment
    abstract fun providesGameSimRepository(repository: GameSimRepository): SimulationContract.GameSimRepository
}