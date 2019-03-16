package com.appdev.jphil.basketball.textcontracts

import com.appdev.jphil.basketball.Player

interface ShotTextContract {

    fun shortMake(shooter: Player, defender: Player, wellDefended: Boolean): String
    fun shortMiss(shooter: Player, defender: Player, wellDefended: Boolean): String
    fun shortFoul(shooter: Player, fouler: Player, shotMade: Boolean): String

    fun midMake(shooter: Player, defender: Player, wellDefended: Boolean): String
    fun midMiss(shooter: Player, defender: Player, wellDefended: Boolean): String
    fun midFoul(shooter: Player, fouler: Player, shotMade: Boolean): String

    fun longMake(shooter: Player, defender: Player, wellDefended: Boolean): String
    fun longMiss(shooter: Player, defender: Player, wellDefended: Boolean): String
    fun longFoul(shooter: Player, fouler: Player, shotMade: Boolean): String

    fun halfCourtMake(shooter: Player, defender: Player, wellDefended: Boolean): String
    fun halfCourtMiss(shooter: Player, defender: Player, wellDefended: Boolean): String
    fun halfCourtFoul(shooter: Player, fouler: Player, shotMade: Boolean): String

    fun beyondHalfCourtMake(shooter: Player, defender: Player, wellDefended: Boolean): String
    fun beyondHalfCourtMiss(shooter: Player, defender: Player, wellDefended: Boolean): String
    fun beyondHalfCourtFoul(shooter: Player, fouler: Player, shotMade: Boolean): String
}