package com.appdev.jphil.basketball.playtext

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.textcontracts.ReboundTextContract

class ReboundPlayText : ReboundTextContract {

    override fun offensiveRebound(player: Player): String {
        return "${player.fullName} grabs the offensive rebound!"
    }

    override fun defensiveRebound(player: Player): String {
        return "${player.fullName} grabs the rebound!"
    }
}
