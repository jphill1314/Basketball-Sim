package com.appdev.jphil.basketballcoach.main.injection

import com.appdev.jphil.basketballcoach.coachoverview.CoachOverviewFragment
import com.appdev.jphil.basketballcoach.coachoverview.CoachOverviewModule
import com.appdev.jphil.basketballcoach.customizeteam.CustomizeFragment
import com.appdev.jphil.basketballcoach.customizeteam.CustomizeModule
import com.appdev.jphil.basketballcoach.game.preview.GamePreviewFragment
import com.appdev.jphil.basketballcoach.game.sim.GameFragment
import com.appdev.jphil.basketballcoach.game.sim.GameModule
import com.appdev.jphil.basketballcoach.main.injection.scopes.PerFragment
import com.appdev.jphil.basketballcoach.newgame.NewGameFragment
import com.appdev.jphil.basketballcoach.newseason.ui.NewSeasonFragment
import com.appdev.jphil.basketballcoach.playeroverview.PlayerOverviewFragment
import com.appdev.jphil.basketballcoach.playeroverview.PlayerOverviewModule
import com.appdev.jphil.basketballcoach.recruiting.ui.RecruitingFragment
import com.appdev.jphil.basketballcoach.recruitoverview.ui.RecruitOverviewFragment
import com.appdev.jphil.basketballcoach.recruitoverview.ui.RecruitOverviewModule
import com.appdev.jphil.basketballcoach.schedule.ui.ScheduleFragment
import com.appdev.jphil.basketballcoach.selectionshow.SelectionShowFragment
import com.appdev.jphil.basketballcoach.startscreen.StartScreenFragment
import com.appdev.jphil.basketballcoach.stats.StatsFragment
import com.appdev.jphil.basketballcoach.team.TeamFragment
import com.appdev.jphil.basketballcoach.tournament.TournamentModule
import com.appdev.jphil.basketballcoach.tournament.ui.TournamentFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @PerFragment
    @ContributesAndroidInjector(modules = [GameModule::class])
    abstract fun gameFragment(): GameFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [PlayerOverviewModule::class])
    abstract fun playerOverviewFragment(): PlayerOverviewFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [CoachOverviewModule::class])
    abstract fun coachOverviewFragment(): CoachOverviewFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun gamePreviewFragment(): GamePreviewFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun composeScheduleFragment(): ScheduleFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [TournamentModule::class])
    abstract fun composeTournamentFragment(): TournamentFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun newSeasonFragment(): NewSeasonFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun selectionShowFragment(): SelectionShowFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun recruitingComposeFragment(): RecruitingFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [RecruitOverviewModule::class])
    abstract fun recruitOverviewComposeFragment(): RecruitOverviewFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun startScreenFragment(): StartScreenFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun newGameFragment(): NewGameFragment

    @PerFragment
    @ContributesAndroidInjector(modules = [CustomizeModule::class])
    abstract fun customizeFragment(): CustomizeFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun teamFragment(): TeamFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun statsFragment(): StatsFragment
}
