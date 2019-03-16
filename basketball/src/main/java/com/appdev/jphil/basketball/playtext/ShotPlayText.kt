package com.appdev.jphil.basketball.playtext

import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketball.textcontracts.ShotTextContract

class ShotPlayText : ShotTextContract {

    override fun shortMake(shooter: Player, defender: Player, wellDefended: Boolean): String {
        return if (wellDefended) {
            "${shooter.fullName} makes the shot from close range, despite great defense from ${defender.fullName}!"
        } else {
            "${shooter.fullName} makes the shot from close range!"
        }
    }

    override fun shortMiss(shooter: Player, defender: Player, wellDefended: Boolean): String {
        return if (wellDefended) {
            "${shooter.fullName} shoots from close, but misses due to the great defense from ${defender.fullName}!"
        } else {
            "${shooter.fullName} shoots from close, but misses!"
        }
    }

    override fun shortFoul(shooter: Player, fouler: Player, shotMade: Boolean): String {
        return if(shotMade) {
            "${shooter.fullName} shoots from close, is fouled, and makes the shot! He'll get a chance " +
                    "at a three point play thanks to the foul by ${fouler.fullName}"
        } else {
            "${shooter.fullName} shoots from close and misses, but ${fouler.fullName} has been called for a foul. " +
                    "${shooter.firstName} will instead get to shoot two free throws."
        }
    }

    override fun midMake(shooter: Player, defender: Player, wellDefended: Boolean): String {
        return if (wellDefended) {
            "${shooter.fullName} makes the shot from mid range, despite great defense from ${defender.fullName}!"
        } else {
            "${shooter.fullName} makes the shot from mid range!"
        }
    }

    override fun midMiss(shooter: Player, defender: Player, wellDefended: Boolean): String {
        return if (wellDefended) {
            "${shooter.fullName} shoots from mid range, but misses due to the great defense from ${defender.fullName}!"
        } else {
            "${shooter.fullName} shoots from mid range, but misses!"
        }
    }

    override fun midFoul(shooter: Player, fouler: Player, shotMade: Boolean): String {
        return if(shotMade) {
            "${shooter.fullName} shoots from mid range, is fouled, and makes the shot! He'll get a chance " +
                    "at a three point play thanks to the foul by ${fouler.fullName}"
        } else {
            "${shooter.fullName} shoots from mid range and misses, but ${fouler.fullName} has been called for a foul. " +
                    "${shooter.firstName} will instead get to shoot two free throws."
        }
    }

    override fun longMake(shooter: Player, defender: Player, wellDefended: Boolean): String {
        return if (wellDefended) {
            "${shooter.fullName} makes the shot from three, despite great defense from ${defender.fullName}!"
        } else {
            "${shooter.fullName} makes the shot from three!"
        }
    }

    override fun longMiss(shooter: Player, defender: Player, wellDefended: Boolean): String {
        return if (wellDefended) {
            "${shooter.fullName} shoots from three, but misses due to the great defense from ${defender.fullName}!"
        } else {
            "${shooter.fullName} shoots from three, but misses!"
        }
    }

    override fun longFoul(shooter: Player, fouler: Player, shotMade: Boolean): String {
        return if(shotMade) {
            "${shooter.fullName} shoots from three, is fouled, and makes the shot! He'll get a chance " +
                    "at a four point play thanks to the foul by ${fouler.fullName}"
        } else {
            "${shooter.fullName} shoots from three and misses, but ${fouler.fullName} has been called for a foul. " +
                    "${shooter.firstName} will instead get to shoot three free throws."
        }
    }

    override fun halfCourtMake(shooter: Player, defender: Player, wellDefended: Boolean): String {
        return "${shooter.fullName} shoots from near half court and he makes it!"
    }

    override fun halfCourtMiss(shooter: Player, defender: Player, wellDefended: Boolean): String {
        return "${shooter.fullName} shoots from half court, but misses!"
    }

    override fun halfCourtFoul(shooter: Player, fouler: Player, shotMade: Boolean): String {
        return if(shotMade) {
            "${shooter.fullName} shoots from near half court, is fouled, and makes the shot! He'll get a chance " +
                    "at a four point play thanks to the foul by ${fouler.fullName}"
        } else {
            "${shooter.fullName} shoots from near half court and misses, but ${fouler.fullName} has been called for a foul. " +
                    "${shooter.firstName} will instead get to shoot three free throws."
        }
    }

    override fun beyondHalfCourtMake(shooter: Player, defender: Player, wellDefended: Boolean): String {
        return "${shooter.fullName} heaves the ball from well beyond half court... AND HE MAKES IT! UNBELIEVABLE SCENES"
    }

    override fun beyondHalfCourtMiss(shooter: Player, defender: Player, wellDefended: Boolean): String {
        return "${shooter.fullName} shoots from well beyond half court, but misses!"
    }

    override fun beyondHalfCourtFoul(shooter: Player, fouler: Player, shotMade: Boolean): String {
        return if(shotMade) {
            "${shooter.fullName} shoots from well beyond half court, is fouled, and makes the shot! He'll get a chance " +
                    "at a four point play thanks to the foul by ${fouler.fullName}"
        } else {
            "${shooter.fullName} shoots from well beyond half court and misses, but ${fouler.fullName} has been called for a foul. " +
                    "${shooter.firstName} will instead get to shoot three free throws."
        }
    }
}