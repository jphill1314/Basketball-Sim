package com.appdev.jphil.basketballcoach.tournamentcompose

import com.appdev.jphil.basketballcoach.tournamentcompose.ui.ComposeTournamentFragment
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
abstract class ComposeTournamentModule {

    companion object {
        @Provides
        @Named("testing")
        fun isTournamentExisting(
            fragment: ComposeTournamentFragment
        ) = fragment.args.doesTournamentExist
    }
}
