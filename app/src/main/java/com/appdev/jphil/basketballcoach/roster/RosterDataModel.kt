package com.appdev.jphil.basketballcoach.roster

import com.appdev.jphil.basketball.Player

data class RosterDataModel(
    val player: Player,
    var isSelected: Boolean = false
)