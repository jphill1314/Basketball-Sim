package com.appdev.jphil.basketballcoach.rankings

import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import dagger.Binds
import dagger.Module

@Module
abstract class RankingsModule {

    @PerFragment
    @Binds
    abstract fun bindsPresenter(presenter: RankingsPresenter): RankingsContract.Presenter

    @PerFragment
    @Binds
    abstract fun bindsRepository(repository: RankingsRepository): RankingsContract.Repository
}
