package com.appdev.jphil.basketballcoach.main

import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment
import com.appdev.jphil.basketballcoach.main.injection.AppComponent
import com.appdev.jphil.basketballcoach.main.injection.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainApplication : HasActivityInjector, HasSupportFragmentInjector, Application() {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder()
            .application(this)
            .build()
        component.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}