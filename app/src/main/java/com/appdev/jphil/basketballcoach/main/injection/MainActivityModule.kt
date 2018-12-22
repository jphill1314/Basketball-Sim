package com.appdev.jphil.basketballcoach.main.injection

import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import com.appdev.jphil.basketballcoach.roster.RosterFragment
import com.appdev.jphil.basketballcoach.roster.RosterModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @PerFragment
    @ContributesAndroidInjector(modules = [RosterModule::class])
    abstract fun rosterFragment(): RosterFragment

}