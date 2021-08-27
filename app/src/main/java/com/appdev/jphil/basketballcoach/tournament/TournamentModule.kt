package com.appdev.jphil.basketballcoach.tournament

import com.appdev.jphil.basketballcoach.tournament.ui.TournamentFragment
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
abstract class TournamentModule {

    companion object {
        @Provides
        @Named("testing")
        fun isTournamentExisting(
            fragment: TournamentFragment
        ) = fragment.args.doesTournamentExist
    }
}
