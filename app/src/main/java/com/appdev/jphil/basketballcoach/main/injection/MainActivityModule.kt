package com.appdev.jphil.basketballcoach.main.injection

import com.appdev.jphil.basketballcoach.main.DaggerNavHost
import com.appdev.jphil.basketballcoach.main.MainActivity
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.ConferenceId
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import com.appdev.jphil.basketballcoach.main.injection.scopes.PerNavHost
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @PerNavHost
    @ContributesAndroidInjector(modules = [FragmentBindingModule::class])
    abstract fun bindsNavHost(): DaggerNavHost

    @Module
    companion object {
        @JvmStatic
        @Provides
        @ConferenceId
        fun providesConferenceId(activity: MainActivity): Int = activity.teamViewModel?.conferenceId ?: 0

        @JvmStatic
        @Provides
        @TeamId
        fun providesTeamId(activity: MainActivity): Int = activity.teamViewModel?.teamId ?: -1
    }
}