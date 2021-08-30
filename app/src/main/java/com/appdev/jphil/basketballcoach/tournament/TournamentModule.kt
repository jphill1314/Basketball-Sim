package com.appdev.jphil.basketballcoach.tournament

import com.appdev.jphil.basketballcoach.tournament.ui.TournamentFragment
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
abstract class TournamentModule {

    companion object {
        @Provides
        @Named("DoesTournamentExist")
        fun isTournamentExisting(
            fragment: TournamentFragment
        ) = fragment.args.doesTournamentExist

        @Provides
        @Named("TournamentId")
        fun tournamentId(
            fragment: TournamentFragment
        ) = fragment.args.tournamentId
    }
}
