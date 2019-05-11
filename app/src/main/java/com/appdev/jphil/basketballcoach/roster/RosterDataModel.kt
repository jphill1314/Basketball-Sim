package com.appdev.jphil.basketballcoach.roster

import com.appdev.jphil.basketball.players.Player

data class RosterDataModel(
    var player: Player,
    var isSelected: Boolean = false
)