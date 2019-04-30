package com.appdev.jphil.basketballcoach.recruiting

import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import dagger.Binds
import dagger.Module

@Module
abstract class RecruitModule {

    @Binds
    @PerFragment
    abstract fun bindsPresenter(presenter: RecruitPresenter): RecruitContract.Presenter

    @Binds
    @PerFragment
    abstract fun bindsRepository(repository: RecruitRepository): RecruitContract.Repository
}