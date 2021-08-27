package com.appdev.jphil.basketballcoach.main

import android.app.Application
import com.appdev.jphil.basketballcoach.BuildConfig
import com.appdev.jphil.basketballcoach.main.injection.AppComponent
import com.appdev.jphil.basketballcoach.main.injection.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class MainApplication @Inject constructor() : Application(), HasAndroidInjector {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>
    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        component = DaggerAppComponent.builder()
            .application(this)
            .build()
        component.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = injector
}
