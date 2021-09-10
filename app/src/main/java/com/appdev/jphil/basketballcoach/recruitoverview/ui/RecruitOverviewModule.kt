package com.appdev.jphil.basketballcoach.recruitoverview.ui

import com.appdev.jphil.basketballcoach.main.injection.qualifiers.RecruitId
import dagger.Module
import dagger.Provides

@Module
class RecruitOverviewModule {

    companion object {
        @Provides
        @RecruitId
        fun provideRecruitId(fragment: RecruitOverviewFragment) = fragment.initialData.recruitId
    }
}
