package com.appdev.jphil.basketballcoach.practice

import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class PracticeModule {

    @Binds
    @PerFragment
    abstract fun bindsPresenter(presenter: PracticePresenter): PracticeContract.Presenter

    @Binds
    @PerFragment
    abstract fun bindsRepository(repository: PracticeRepository): PracticeContract.Repository
}