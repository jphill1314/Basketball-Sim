package com.appdev.jphil.basketballcoach.main.injection

import com.appdev.jphil.basketballcoach.coaches.CoachesFragment
import com.appdev.jphil.basketballcoach.coaches.CoachesModule
import com.appdev.jphil.basketballcoach.game.GameFragment
import com.appdev.jphil.basketballcoach.game.GameModule
import com.appdev.jphil.basketballcoach.main.MainActivity
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.ConferenceId
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import com.appdev.jphil.basketballcoach.main.injection.scopes.PerActivity
import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import com.appdev.jphil.basketballcoach.playeroverview.PlayerOverviewFragment
import com.appdev.jphil.basketballcoach.playeroverview.PlayerOverviewModule
import com.appdev.jphil.basketballcoach.practice.PracticeFragment
import com.appdev.jphil.basketballcoach.practice.PracticeModule
import com.appdev.jphil.basketballcoach.roster.RosterFragment
import com.appdev.jphil.basketballcoach.roster.RosterModule
import com.appdev.jphil.basketballcoach.schedule.ScheduleFragment
import com.appdev.jphil.basketballcoach.schedule.ScheduleModule
import com.appdev.jphil.basketballcoach.standings.StandingsFragment
import com.appdev.jphil.basketballcoach.standings.StandingsModule
import com.appdev.jphil.basketballcoach.strategy.StrategyFragment
import com.appdev.jphil.basketballcoach.strategy.StrategyModule
import com.appdev.jphil.basketballcoach.tournament.TournamentFragment
import com.appdev.jphil.basketballcoach.tournament.TournamentModule
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @PerFragment
    @ContributesAndroidInjector(modules = [RosterModule::class])
    abstract fun rosterFragment(): RosterFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [ScheduleModule::class])
    abstract fun scheduleFragment(): ScheduleFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [GameModule::class])
    abstract fun gameFragment(): GameFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [StrategyModule::class])
    abstract fun strategyFragment(): StrategyFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [StandingsModule::class])
    abstract fun standingsFragment(): StandingsFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [PlayerOverviewModule::class])
    abstract fun playerOverviewFragment(): PlayerOverviewFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [TournamentModule::class])
    abstract fun tournamentFragment(): TournamentFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [CoachesModule::class])
    abstract fun coachesFragment(): CoachesFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [PracticeModule::class])
    abstract fun practiceFragment(): PracticeFragment

    @Module
    companion object {
        @JvmStatic
        @Provides
        @ConferenceId
        fun providesConferenceId(activity: MainActivity): Int = activity.getConferenceId()

        @JvmStatic
        @Provides
        @TeamId
        fun providesTeamId(activity: MainActivity): Int = activity.getTeamId()
    }
}