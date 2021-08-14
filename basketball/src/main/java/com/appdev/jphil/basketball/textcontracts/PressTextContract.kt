package com.appdev.jphil.basketball.textcontracts

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.teams.Team

interface PressTextContract {

    fun passToFastBreak(passer: Player, target: Player): String
    fun passToWalkToFrontCourt(passer: Player, target: Player): String
    fun passToFrontCourt(passer: Player, target: Player): String
    fun passToBackCourt(passer: Player, target: Player): String

    fun inboundToFastBreak(passer: Player, target: Player, isFirstPress: Boolean, defense: Team): String
    fun inboundToWalkToFrontCourt(passer: Player, target: Player, isFirstPress: Boolean, defense: Team): String
    fun inboundToFrontCourt(passer: Player, target: Player, isFirstPress: Boolean, defense: Team): String
    fun inboundToBackCourt(passer: Player, target: Player, isFirstPress: Boolean, defense: Team): String

    fun stolenPass(passer: Player, target: Player, stealer: Player): String
    fun stolenInbound(passer: Player, target: Player, stealer: Player): String

    fun badPass(passer: Player, target: Player, stealer: Player): String
    fun badInbound(passer: Player, target: Player, stealer: Player): String

    fun mishandledPass(passer: Player, target: Player, stealer: Player): String
    fun mishandledInbound(passer: Player, target: Player, stealer: Player): String

    fun justDribbling(player: Player): String
}
