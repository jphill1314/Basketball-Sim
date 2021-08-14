package com.appdev.jphil.basketball.playtext

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.textcontracts.PressTextContract

class PressPlayText : PressTextContract {

    override fun passToFastBreak(passer: Player, target: Player): String {
        return "${passer.fullName} passes the ball to ${target.fullName} " +
            "and it's a great pass! ${target.firstName} has a clear path to the basket!"
    }

    override fun passToWalkToFrontCourt(passer: Player, target: Player): String {
        return "${passer.fullName} passes the ball to ${target.fullName}, " +
            "who walks the ball into the frontcourt."
    }

    override fun passToFrontCourt(passer: Player, target: Player): String {
        return "${passer.fullName} passes the ball to ${target.fullName}, " +
            "who is in the frontcourt, breaking the press."
    }

    override fun passToBackCourt(passer: Player, target: Player): String {
        return "${passer.fullName} passes the ball to ${target.fullName}."
    }

    override fun inboundToFastBreak(passer: Player, target: Player, isFirstPress: Boolean, defense: Team): String {
        val firstPress = if (isFirstPress) "${defense.name} will pick up the press here as " else ""
        return "$firstPress${passer.fullName} inbounds the ball to ${target.fullName} " +
            "and it's a great pass! ${target.firstName} has a clear path to the basket!"
    }

    override fun inboundToWalkToFrontCourt(passer: Player, target: Player, isFirstPress: Boolean, defense: Team): String {
        val firstPress = if (isFirstPress) "${defense.name} will pick up the press here as " else ""
        return "$firstPress${passer.fullName} inbounds the ball to ${target.fullName}, " +
            "who walks the ball into the frontcourt."
    }

    override fun inboundToFrontCourt(passer: Player, target: Player, isFirstPress: Boolean, defense: Team): String {
        val firstPress = if (isFirstPress) "${defense.name} will pick up the press here as " else ""
        return "$firstPress${passer.fullName} inbounds the ball to ${target.fullName}, " +
            "who is in the frontcourt, breaking the press."
    }

    override fun inboundToBackCourt(passer: Player, target: Player, isFirstPress: Boolean, defense: Team): String {
        val firstPress = if (isFirstPress) "${defense.name} will pick up the press here as " else ""
        return "$firstPress${passer.fullName} inbounds the ball to ${target.fullName}."
    }

    override fun stolenPass(passer: Player, target: Player, stealer: Player): String {
        return "${passer.fullName} passes the ball to ${target.fullName}, " +
            "but it is stolen by ${stealer.fullName}!"
    }

    override fun stolenInbound(passer: Player, target: Player, stealer: Player): String {
        return "${passer.fullName} inbounds the ball to ${target.fullName}, " +
            "but it is stolen by ${stealer.fullName}!"
    }

    override fun badPass(passer: Player, target: Player, stealer: Player): String {
        return "${passer.fullName} passes the ball to ${target.fullName}, " +
            "but it's a bad pass and is stolen by ${stealer.fullName}!"
    }

    override fun badInbound(passer: Player, target: Player, stealer: Player): String {
        return "${passer.fullName} inbounds the ball to ${target.fullName}, " +
            "but it's a bad pass and is stolen by ${stealer.fullName}!"
    }

    override fun mishandledPass(passer: Player, target: Player, stealer: Player): String {
        return "${passer.fullName} passes the ball to ${target.fullName}, " +
            "but it's a bad pass and is stolen by ${stealer.fullName}!"
    }

    override fun mishandledInbound(passer: Player, target: Player, stealer: Player): String {
        return "${passer.fullName} inbounds the ball to ${target.fullName}, " +
            "but it's a bad pass and is stolen by ${stealer.fullName}!"
    }

    override fun justDribbling(player: Player): String {
        return "${player.fullName} is dribbling with the ball."
    }
}
