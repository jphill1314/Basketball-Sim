package com.appdev.jphil.basketballcoach.strategy

import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import dagger.Binds
import dagger.Module

@Module
abstract class StrategyModule {

    @Binds
    @PerFragment
    abstract fun bindPresenter(presenter: StrategyPresenter): StrategyContract.Presenter

    @Binds
    @PerFragment
    abstract fun bindRepository(repository: StrategyRepository): StrategyContract.Repository
}