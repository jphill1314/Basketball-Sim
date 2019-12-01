package com.appdev.jphil.basketballcoach.main.injection

import android.app.Application
import com.appdev.jphil.basketballcoach.database.DatabaseModule
import com.appdev.jphil.basketballcoach.main.MainApplication
import com.appdev.jphil.basketballcoach.main.injection.scopes.PerApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@PerApplication
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    DatabaseModule::class
])
interface AppComponent : AndroidInjector<MainApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }
}