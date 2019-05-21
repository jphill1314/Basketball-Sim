package com.appdev.jphil.basketballcoach.strategy

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketballcoach.MVPContract

interface StrategyContract {

    interface View : MVPContract.View {
        fun updateStrategy(strategyDataModels: List<StrategyDataModel>)
    }

    interface Presenter : MVPContract.Presenter<View>, Adapter.Out {
        fun fetchStrategy()
        fun onStrategyLoaded(coach: Coach)
    }

    interface Repository : MVPContract.Repository<Presenter> {
        fun loadStrategy()
        fun saveStrategy(coach: Coach)
    }

    interface Adapter {
        interface Out {
            fun onPaceChanged(pace: Int)
            fun onOffenseFavorsThreesChanged(favorsThrees: Int)
            fun onAggressionChanged(aggression: Int)
            fun onDefenseFavorsThreesChanged(favorsThrees: Int)
            fun onPressFrequencyChanged(frequency: Int)
            fun onPressAggressionChanged(aggression: Int)
            fun onIntentionallyFoulToggled(isChecked: Boolean)
        }
    }
}