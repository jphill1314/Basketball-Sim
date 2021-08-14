package com.appdev.jphil.basketball.plays.utils

import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.teams.Team
import kotlin.math.max
import kotlin.random.Random

class PassingUtils(
    private val homeTeam: Team,
    private val awayTeam: Team,
    private val randomBound: Int
) {

    private lateinit var offense: Team
    private lateinit var defense: Team

    fun getTarget(homeTeamHasBall: Boolean, playerWithBall: Int): Int {
        setTeams(homeTeamHasBall)
        val favorsThrees = offense.offenseFavorsThrees
        val pg = if (playerWithBall == 1) 0 else getPassTargetChance(favorsThrees, offense.getPlayerAtPosition(1))
        val sg = if (playerWithBall == 2) 0 else getPassTargetChance(favorsThrees, offense.getPlayerAtPosition(2))
        val sf = if (playerWithBall == 3) 0 else getPassTargetChance(favorsThrees, offense.getPlayerAtPosition(3))
        val pf = if (playerWithBall == 4) 0 else getPassTargetChance(favorsThrees, offense.getPlayerAtPosition(4))
        val c = if (playerWithBall == 5) 0 else getPassTargetChance(favorsThrees, offense.getPlayerAtPosition(5))

        return if (c > pf && c > sf && c > sg && c > pg) {
            5
        } else if (pf > c && pf > sf && pf > sg && pf > pg) {
            4
        } else if (sf > c && sf > pf && sf > sg && sf > pg) {
            3
        } else if (sg > c && sg > pf && sg > sf && sg > pg || playerWithBall == 1) {
            2
        } else {
            1
        }
    }

    private fun getPassTargetChance(favorsThrees: Int, player: Player): Int {
        val three = player.longRangeShot * (favorsThrees / 100.0)
        val two = max(player.closeRangeShot, player.postMove) * ((100 - favorsThrees) / 100.0)
        return (player.offBallMovement + three + two + Random.nextInt(randomBound)).toInt()
    }

    fun getInbounder(homeTeamHasBall: Boolean): Int {
        setTeams(homeTeamHasBall)
        val pg = offense.getPlayerAtPosition(1).passing + Random.nextInt(randomBound)
        val sg = offense.getPlayerAtPosition(2).passing + Random.nextInt(randomBound)
        val sf = offense.getPlayerAtPosition(3).passing + Random.nextInt(randomBound)
        val pf = offense.getPlayerAtPosition(4).passing + Random.nextInt(randomBound)
        val c = offense.getPlayerAtPosition(5).passing + Random.nextInt(randomBound)

        return if (c > pf && c > sf && c > sg && c > pg) {
            5
        } else if (pf > c && pf > sf && pf > sg && pf > pg) {
            4
        } else if (sf > c && sf > pf && sf > sg && sf > pg) {
            3
        } else if (sg > c && sg > pf && sg > sf && sg > pg) {
            2
        } else {
            1
        }
    }

    private fun setTeams(homeTeamHasBall: Boolean) {
        if (homeTeamHasBall) {
            offense = homeTeam
            defense = awayTeam
        } else {
            offense = awayTeam
            defense = homeTeam
        }
    }
}
