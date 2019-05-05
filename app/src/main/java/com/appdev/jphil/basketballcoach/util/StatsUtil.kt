package com.appdev.jphil.basketballcoach.util

import com.appdev.jphil.basketballcoach.database.player.GameStatsEntity

class StatsUtil {
    var gamesPlayed = 0
    var totalTimePlayed = 0
    var totalTwoPointAttempts = 0
    var totalTwoPointMakes = 0
    var totalThreePointAttempts = 0
    var totalThreePointMakes = 0
    var totalAssists = 0
    var totalOffensiveRebounds = 0
    var totalDefensiveRebounds = 0
    var totalTurnovers = 0
    var totalSteals = 0
    var totalFouls = 0
    var totalFreeThrowShots = 0
    var totalFreeThrowMakes = 0


    fun calculateTotals(gamesStats: List<GameStatsEntity>) {
        gamesStats.forEach {
            gamesPlayed++
            totalTimePlayed += it.timePlayed
            totalTwoPointAttempts += it.twoPointAttempts
            totalTwoPointMakes += it.twoPointMakes
            totalThreePointAttempts += it.threePointAttempts
            totalThreePointMakes += it.threePointMakes
            totalAssists += it.assists
            totalOffensiveRebounds += it.offensiveRebounds
            totalDefensiveRebounds += it.defensiveRebounds
            totalTurnovers += it.turnovers
            totalSteals += it.steals
            totalFouls += it.fouls
            totalFreeThrowShots += it.freeThrowShots
            totalFreeThrowMakes += it.freeThrowMakes
        }
    }

    fun getMinutesAvg(): String {
      return if (gamesPlayed == 0) {
          "0"
      } else {
          FORMAT.format(totalTimePlayed / 60.0 / gamesPlayed)
      }
    }

    fun getPointsAvg(): String {
        return if (gamesPlayed == 0) {
            "0"
        } else {
            val points = 2 * totalTwoPointMakes + 3 * totalThreePointMakes + totalFreeThrowMakes
            val avg = points / gamesPlayed.toDouble()
            FORMAT.format(avg)
        }
    }

    fun getAssistAvg(): String {
        return if (gamesPlayed == 0) {
            "0"
        } else {
            FORMAT.format(totalAssists / gamesPlayed.toDouble())
        }
    }

    fun getReboundAvg(): String {
        return if (gamesPlayed == 0) {
            "0"
        } else {
            FORMAT.format((totalOffensiveRebounds + totalDefensiveRebounds) / gamesPlayed.toDouble())
        }
    }

    fun getStealAvg(): String {
        return if (gamesPlayed == 0) {
            "0"
        } else {
            FORMAT.format(totalSteals / gamesPlayed.toDouble())
        }
    }

    fun getFoulAvg(): String {
        return if (gamesPlayed == 0) {
            "0"
        } else {
            FORMAT.format(totalFouls / gamesPlayed.toDouble())
        }
    }

    private companion object {
        const val FORMAT = "%.1f"
    }
}