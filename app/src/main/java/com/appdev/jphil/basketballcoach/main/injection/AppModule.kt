package com.appdev.jphil.basketballcoach.main.injection

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.appdev.jphil.basketballcoach.main.MainActivity
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.AppContext
import com.appdev.jphil.basketballcoach.main.injection.scopes.PerActivity
import com.appdev.jphil.basketballcoach.main.injection.scopes.PerApplication
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppModule {

    @Binds
    @AppContext
    @PerApplication
    abstract fun providesContext(application: Application): Context

    @PerActivity
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun providesMainActivity(): MainActivity

    @Module
    companion object {
        @JvmStatic
        @Provides
        @PerApplication
        fun providesResources(@AppContext context: Context): Resources = context.resources
    }
}
