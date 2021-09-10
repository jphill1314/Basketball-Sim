package com.appdev.jphil.basketballcoach.recruiting.ui

import androidx.annotation.StringRes
import com.appdev.jphil.basketballcoach.compose.arch.UiModel

data class RecruitModel(
    val id: Int,
    val name: String,
    @StringRes val position: Int,
    val type: Int,
    val rating: Int,
    val potential: Int,
    val interest: Int,
    val recruitmentLevel: Int,
    @StringRes val status: Int
) : UiModel {

    interface Interactor {
        fun onRecruitClicked(id: Int)
    }
}

data class TeamStateModel(
    val returningPGs: Int,
    val committedPGs: Int,
    val recruitingPGs: Int,
    val returningSGs: Int,
    val committedSGs: Int,
    val recruitingSGs: Int,
    val returningSFs: Int,
    val committedSFs: Int,
    val recruitingSFs: Int,
    val returningPFs: Int,
    val committedPFs: Int,
    val recruitingPFs: Int,
    val returningCs: Int,
    val committedCs: Int,
    val recruitingCs: Int
) : UiModel {

    interface Interactor {
        fun onPositionClicked(pos: Int)
        fun onShowRecruitingClicked()
        fun clearFilters()
    }
}
