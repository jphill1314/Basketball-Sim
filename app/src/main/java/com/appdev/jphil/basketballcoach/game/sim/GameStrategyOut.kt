package com.appdev.jphil.basketballcoach.game.sim

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.strategy.StrategyContract

class GameStrategyOut : StrategyContract.Adapter.Out {
    lateinit var coach: Coach
    lateinit var userTeam: Team

    override fun onPaceChanged(pace: Int) {
        coach.paceGame = pace + Coach.minimumPace
    }

    override fun onOffenseFavorsThreesChanged(favorsThrees: Int) {
        coach.offenseFavorsThreesGame = favorsThrees
    }

    override fun onAggressionChanged(aggression: Int) {
        coach.aggressionGame = aggression
    }

    override fun onDefenseFavorsThreesChanged(favorsThrees: Int) {
        coach.defenseFavorsThreesGame = favorsThrees
    }

    override fun onPressFrequencyChanged(frequency: Int) {
        coach.pressFrequencyGame = frequency
    }

    override fun onPressAggressionChanged(aggression: Int) {
        coach.pressAggressionGame = aggression
    }

    override fun onMoveQuicklyToggled(isChecked: Boolean) {
        coach.shouldHurry = isChecked
    }

    override fun onWasteTimeToggled(isChecked: Boolean) {
        coach.shouldWasteTime = isChecked
    }

    override fun onIntentionallyFoulToggled(isChecked: Boolean) {
        coach.intentionallyFoul = isChecked
        userTeam.intentionallyFoul = true
    }
}
