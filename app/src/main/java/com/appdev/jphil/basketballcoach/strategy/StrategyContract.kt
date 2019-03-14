package com.appdev.jphil.basketballcoach.strategy

import com.appdev.jphil.basketball.Coach
import com.appdev.jphil.basketballcoach.MVPContract

interface StrategyContract {

    interface View : MVPContract.View {
        fun updateStrategy(strategyDataModel: StrategyDataModel)
    }

    interface Presenter : MVPContract.Presenter<View> {
        fun fetchStrategy()
        fun onStrategyLoaded(coach: Coach)
        fun onPaceChanged(pace: Int)
        fun onOffenseFavorsThreesChanged(favorsThrees: Int)
        fun onAggressionChanged(aggression: Int)
        fun onDefenseFavorsThreesChanged(favorsThrees: Int)
        fun onPressFrequencyChanged(frequency: Int)
        fun onPressAggressionChanged(aggression: Int)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun loadStrategy()
        fun saveStrategy(coach: Coach)
    }
}