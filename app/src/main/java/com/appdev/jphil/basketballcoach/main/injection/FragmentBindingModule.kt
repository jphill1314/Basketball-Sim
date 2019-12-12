package com.appdev.jphil.basketballcoach.main.injection

import com.appdev.jphil.basketballcoach.coaches.CoachesFragment
import com.appdev.jphil.basketballcoach.coaches.CoachesModule
import com.appdev.jphil.basketballcoach.coachoverview.CoachOverviewFragment
import com.appdev.jphil.basketballcoach.coachoverview.CoachOverviewModule
import com.appdev.jphil.basketballcoach.game.preview.GamePreviewFragment
import com.appdev.jphil.basketballcoach.game.sim.GameFragment
import com.appdev.jphil.basketballcoach.game.sim.GameModule
import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import com.appdev.jphil.basketballcoach.playeroverview.PlayerOverviewFragment
import com.appdev.jphil.basketballcoach.playeroverview.PlayerOverviewModule
import com.appdev.jphil.basketballcoach.practice.PracticeFragment
import com.appdev.jphil.basketballcoach.practice.PracticeModule
import com.appdev.jphil.basketballcoach.rankings.RankingsFragment
import com.appdev.jphil.basketballcoach.rankings.RankingsModule
import com.appdev.jphil.basketballcoach.recruiting.RecruitFragment
import com.appdev.jphil.basketballcoach.recruiting.RecruitModule
import com.appdev.jphil.basketballcoach.recruitoverview.RecruitOverviewFragment
import com.appdev.jphil.basketballcoach.recruitoverview.RecruitOverviewModule
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
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

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

    @PerFragment
    @ContributesAndroidInjector(modules = [RecruitModule::class])
    abstract fun recruitFragment(): RecruitFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [RecruitOverviewModule::class])
    abstract fun recruitOverviewFragment(): RecruitOverviewFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [CoachOverviewModule::class])
    abstract fun coachOverviewFragment(): CoachOverviewFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun gamePreviewFragment(): GamePreviewFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [RankingsModule::class])
    abstract fun rankingFragment(): RankingsFragment
}