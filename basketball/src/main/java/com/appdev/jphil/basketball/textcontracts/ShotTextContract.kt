package com.appdev.jphil.basketball.textcontracts

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.plays.Shot

interface ShotTextContract {

    fun shortMake(shooter: Player, defender: Player, wellDefended: Boolean, shot: Shot): String
    fun shortMiss(shooter: Player, defender: Player, wellDefended: Boolean, shot: Shot): String
    fun shortFoul(shooter: Player, fouler: Player, shotMade: Boolean, shot: Shot): String

    fun midMake(shooter: Player, defender: Player, wellDefended: Boolean, shot: Shot): String
    fun midMiss(shooter: Player, defender: Player, wellDefended: Boolean, shot: Shot): String
    fun midFoul(shooter: Player, fouler: Player, shotMade: Boolean, shot: Shot): String

    fun longMake(shooter: Player, defender: Player, wellDefended: Boolean, shot: Shot): String
    fun longMiss(shooter: Player, defender: Player, wellDefended: Boolean, shot: Shot): String
    fun longFoul(shooter: Player, fouler: Player, shotMade: Boolean, shot: Shot): String

    fun halfCourtMake(shooter: Player, defender: Player, wellDefended: Boolean, shot: Shot): String
    fun halfCourtMiss(shooter: Player, defender: Player, wellDefended: Boolean, shot: Shot): String
    fun halfCourtFoul(shooter: Player, fouler: Player, shotMade: Boolean, shot: Shot): String

    fun beyondHalfCourtMake(shooter: Player, defender: Player, wellDefended: Boolean, shot: Shot): String
    fun beyondHalfCourtMiss(shooter: Player, defender: Player, wellDefended: Boolean, shot: Shot): String
    fun beyondHalfCourtFoul(shooter: Player, fouler: Player, shotMade: Boolean, shot: Shot): String
}
