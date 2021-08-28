package com.appdev.jphil.basketballcoach.schedule.ui

import com.appdev.jphil.basketballcoach.compose.arch.UiModel

data class ScheduleUiModel(
    val id: Int,
    val gameNumber: Int,
    val topTeamName: String,
    val bottomTeamName: String,
    val topTeamScore: String,
    val bottomTeamScore: String,
    val isShowButtons: Boolean,
    val isFinal: Boolean,
    val isSelectedTeamWinner: Boolean,
    val isHomeTeamUser: Boolean
) : UiModel {
    interface Interactor {
        fun toggleShowButtons(uiModel: ScheduleUiModel)
        fun simulateGame(uiModel: ScheduleUiModel)
        fun playGame(uiModel: ScheduleUiModel)
    }
}

data class TournamentUiModel(
    val name: String,
    val isExisting: Boolean
) : UiModel {
    interface Interactor {
        fun openTournament(isExisting: Boolean)
    }
}

data class NationalChampionshipUiModel(
    val isExisting: Boolean
): UiModel {
    interface Interactor {
        fun openNationalChampionship(isExisting: Boolean)
    }
}

object FinishSeasonUiModel : UiModel {
    interface Interactor {
        fun startNewSeason()
    }
}

data class SimDialogUiModel(
    val isSimActive: Boolean,
    val isSimulatingToGame: Boolean,
    val numberOfGamesToSim: Int,
    val numberOfGamesSimmed: Int,
    val gameModels: List<UiModel>
) : UiModel {
    interface Interactor {
        fun onDismissSimDialog()
        fun onStartGame(uiModel: ScheduleUiModel)
    }
}
