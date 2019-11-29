package com.appdev.jphil.basketballcoach.playeroverview

import com.appdev.jphil.basketballcoach.main.injection.qualifiers.PlayerId
import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class PlayerOverviewModule {

    @Binds
    @PerFragment
    abstract fun bindsPresenter(presenter: PlayerOverviewPresenter): PlayerOverviewContract.Presenter

    @Binds
    @PerFragment
    abstract fun bindsRepository(repository: PlayerOverviewRepository): PlayerOverviewContract.Repository

    @Module
    companion object {
        @Provides
        @PerFragment
        @JvmStatic
        @PlayerId
        fun providesPlayerId(fragment: PlayerOverviewFragment): Int = fragment.args.playerId
    }
}