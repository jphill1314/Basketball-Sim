package com.appdev.jphil.basketballcoach.customizeteam

import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
abstract class CustomizeModule {

    companion object {
        @Provides
        fun test(fragment: CustomizeFragment) = fragment.args

        @Provides
        @Named("initialTeam")
        fun providesTeam(
            fragment: CustomizeFragment
        ) = fragment.args.initialTeam

        @Provides
        @Named("conferences")
        fun providesConferences(
            fragment: CustomizeFragment
        ) = fragment.args.conferences.conferences
    }
}
