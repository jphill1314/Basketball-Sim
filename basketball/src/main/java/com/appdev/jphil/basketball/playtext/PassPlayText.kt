package com.appdev.jphil.basketball.playtext

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.textcontracts.PassTextContract

class PassPlayText : PassTextContract {

    override fun successfulPass(passer: Player, target: Player): String {
        return "${passer.fullName} passes the ball to ${target.fullName}."
    }

    override fun successfulInbound(passer: Player, target: Player): String {
        return "${passer.fullName} inbounds the ball to ${target.fullName}."
    }

    override fun successfulPassBackcourt(passer: Player, target: Player): String {
        return "${passer.fullName} passes the ball to ${target.fullName} who brings the ball into the front court."
    }

    override fun successfulInboundBackcourt(passer: Player, target: Player): String {
        return "${passer.fullName} inbounds the ball to ${target.fullName} who brings the ball into the front court."
    }

    override fun badPass(passer: Player, target: Player): String {
        return "${passer.fullName} turns the ball over with a horrid pass!"
    }

    override fun badInbound(passer: Player, target: Player): String {
        return "${passer.fullName} turns the ball over with a bad inbounds pass!"
    }

    override fun mishandledPass(passer: Player, target: Player): String {
        return "${passer.fullName} passes the ball to ${target.fullName} who cannot handle the pass and turns it over!"
    }

    override fun mishandledInbound(passer: Player, target: Player): String {
        return "${passer.fullName} inbounds the ball to ${target.fullName} who cannot handle the pass and turns it over!"
    }

    override fun stolenPass(passer: Player, target: Player, stealer: Player): String {
        return "${passer.fullName} passes the ball to ${target.fullName}, but it is stolen by ${stealer.fullName}!"
    }

    override fun stolenInbound(passer: Player, target: Player, stealer: Player): String {
        return "${passer.fullName} inbounds the ball to ${target.fullName}, but it is stolen by ${stealer.fullName}!"
    }

    override fun justDribbling(player: Player): String {
        return "${player.fullName} is dribbling the ball."
    }
}