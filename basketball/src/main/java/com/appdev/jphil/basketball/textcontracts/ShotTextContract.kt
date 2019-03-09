package com.appdev.jphil.basketball.textcontracts

import com.appdev.jphil.basketball.Player

interface ShotTextContract {

    fun shortMake(shooter: Player): String
    fun shortMiss(shooter: Player): String
    fun shortFoul(shooter: Player, fouler: Player, shotMade: Boolean): String

    fun midMake(shooter: Player): String
    fun midMiss(shooter: Player): String
    fun midFoul(shooter: Player, fouler: Player, shotMade: Boolean): String

    fun longMake(shooter: Player): String
    fun longMiss(shooter: Player): String
    fun longFoul(shooter: Player, fouler: Player, shotMade: Boolean): String

    fun halfCourtMake(shooter: Player): String
    fun halfCourtMiss(shooter: Player): String
    fun halfCourtFoul(shooter: Player, fouler: Player, shotMade: Boolean): String

    fun beyondHalfCourtMake(shooter: Player): String
    fun beyondHalfCourtMiss(shooter: Player): String
    fun beyondHalfCourtFoul(shooter: Player, fouler: Player, shotMade: Boolean): String
}