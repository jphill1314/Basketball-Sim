package com.appdev.jphil.basketballcoach.team

import androidx.lifecycle.viewModelScope
import com.appdev.jphil.basketball.coaches.CoachType
import com.appdev.jphil.basketballcoach.compose.arch.BasicComposePresenter
import com.appdev.jphil.basketballcoach.compose.arch.Event
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class TeamPresenter(
    private val params: Params,
    private val teamRepository: TeamRepository
): BasicComposePresenter<TeamContract.DataState>(), TeamContract.Interactor {

    data class Params @Inject constructor(
        @TeamId val teamId: Int
    )

    override val initialDataState = TeamContract.DataState()

    init {
        viewModelScope.launch {
            teamRepository.getTeamRelations(params.teamId).collect { relations ->
                updateState {
                    copy(
                        team = relations.teamEntity,
                        players = relations.playerEntities.map { it.playerEntity }.sortedBy { it.rosterIndex },
                        coaches = relations.coachEntities.map { it.coachRelations }
                    )
                }
            }
        }
    }

    override fun onPlayerSelected(index: Int) {
        when (val selected = state.value.selectedPlayerIndex) {
            -1 -> updateState { copy(selectedPlayerIndex = index) }
            index -> updateState { copy(selectedPlayerIndex = -1) }
            else -> {
                val player1 = state.value.players.first { it.rosterIndex == selected }
                val player2 = state.value.players.first { it.rosterIndex == index }

                viewModelScope.launch {
                    teamRepository.updatePlayers(
                        listOf(
                            player1.copy(rosterIndex = index),
                            player2.copy(rosterIndex = selected)
                        )
                    )
                    updateState { copy(selectedPlayerIndex = -1) }
                }
            }
        }
    }

    override fun onPlayerLongPressed(playerId: Int) {
        if (playerId != -1) {
            sendEvent(LaunchPlayerDetail(playerId))
        }
    }

    override fun onCoachSelected(coachId: Int) {
        if (coachId != -1) {
            sendEvent(LaunchCoachDetail(coachId))
        }
    }

    override fun onPaceChanged(pace: Int) {
        viewModelScope.launch {
            teamRepository.updateCoach(
                getHeadCoach().copy(pace = pace)
            )
        }
    }

    override fun onOffenseFavorsThreesChanged(favorsThrees: Int) {
        viewModelScope.launch {
            teamRepository.updateCoach(
                getHeadCoach().copy(offenseFavorsThrees = favorsThrees)
            )
        }
    }

    override fun onAggressionChanged(aggression: Int) {
        viewModelScope.launch {
            teamRepository.updateCoach(
                getHeadCoach().copy(aggression = aggression)
            )
        }
    }

    override fun onDefenseFavorsThreesChanged(favorsThrees: Int) {
        viewModelScope.launch {
            teamRepository.updateCoach(
                getHeadCoach().copy(defenseFavorsThrees = favorsThrees)
            )
        }
    }

    override fun onPressFrequencyChanged(frequency: Int) {
        viewModelScope.launch {
            teamRepository.updateCoach(
                getHeadCoach().copy(pressFrequency = frequency)
            )
        }
    }

    override fun onPressAggressionChanged(aggression: Int) {
        viewModelScope.launch {
            teamRepository.updateCoach(
                getHeadCoach().copy(pressAggression = aggression)
            )
        }
    }

    override fun onIntentionallyFoulToggled(isChecked: Boolean) { /* No op */ }

    override fun onMoveQuicklyToggled(isChecked: Boolean) { /* No op */ }

    override fun onWasteTimeToggled(isChecked: Boolean) { /* No op */ }

    private fun getHeadCoach() = state.value.coaches.first { it.type == CoachType.HEAD_COACH }

    data class LaunchPlayerDetail(val playerId: Int) : Event
    data class LaunchCoachDetail(val coachId: Int): Event
}
