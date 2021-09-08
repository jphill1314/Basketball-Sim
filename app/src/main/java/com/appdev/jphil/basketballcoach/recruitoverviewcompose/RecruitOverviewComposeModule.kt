package com.appdev.jphil.basketballcoach.recruitoverviewcompose

import com.appdev.jphil.basketballcoach.main.injection.qualifiers.RecruitId
import dagger.Module
import dagger.Provides

@Module
class RecruitOverviewComposeModule {

    companion object {
        @Provides
        @RecruitId
        fun provideRecruitId(fragment: RecruitOverviewComposeFragment) = fragment.extras.recruitId
    }
}
