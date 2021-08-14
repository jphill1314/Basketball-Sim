package com.appdev.jphil.basketball.textcontracts

import com.appdev.jphil.basketball.players.Player

interface PassTextContract {

    fun successfulPass(passer: Player, target: Player): String
    fun successfulInbound(passer: Player, target: Player): String
    fun successfulPassBackcourt(passer: Player, target: Player): String
    fun successfulInboundBackcourt(passer: Player, target: Player): String

    fun badPass(passer: Player, target: Player): String
    fun badInbound(passer: Player, target: Player): String
    fun mishandledPass(passer: Player, target: Player): String
    fun mishandledInbound(passer: Player, target: Player): String

    fun stolenPass(passer: Player, target: Player, stealer: Player): String
    fun stolenInbound(passer: Player, target: Player, stealer: Player): String

    fun justDribbling(player: Player): String
}
