package com.appdev.jphil.basketballcoach.team

import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity
import com.appdev.jphil.basketballcoach.database.team.TeamEntity

interface TeamContract {

    interface Interactor : StrategyInteractor {
        fun onPlayerSelected(index: Int)
        fun onPlayerLongPressed(playerId: Int)
        fun onCoachSelected(coachId: Int)
    }

    data class DataState(
        val team: TeamEntity? = null,
        val players: List<PlayerEntity> = emptyList(),
        val coaches: List<CoachEntity> = emptyList(),
        val selectedPlayerIndex: Int = -1
    ) : com.appdev.jphil.basketballcoach.compose.arch.DataState
}

interface StrategyInteractor {
    fun onPaceChanged(pace: Int)
    fun onOffenseFavorsThreesChanged(favorsThrees: Int)
    fun onAggressionChanged(aggression: Int)
    fun onDefenseFavorsThreesChanged(favorsThrees: Int)
    fun onPressFrequencyChanged(frequency: Int)
    fun onPressAggressionChanged(aggression: Int)
    fun onIntentionallyFoulToggled(isChecked: Boolean)
    fun onMoveQuicklyToggled(isChecked: Boolean)
    fun onWasteTimeToggled(isChecked: Boolean)
}
