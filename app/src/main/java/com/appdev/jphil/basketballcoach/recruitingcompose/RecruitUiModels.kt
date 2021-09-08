package com.appdev.jphil.basketballcoach.recruitingcompose

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
    val status: String
) : UiModel {

    interface Interactor {
        fun onRecruitClicked(id: Int)
    }
}

data class TeamStateModel(
    val returningPGs: Int,
    val committedPGs: Int,
    val returningSGs: Int,
    val committedSGs: Int,
    val returningSFs: Int,
    val committedSFs: Int,
    val returningPFs: Int,
    val committedPFs: Int,
    val returningCs: Int,
    val committedCs: Int
) : UiModel
