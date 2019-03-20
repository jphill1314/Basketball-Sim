package com.appdev.jphil.basketball

import com.appdev.jphil.basketball.game.CoachTalk
import java.util.*

class Team(
    val teamId: Int,
    val name: String,
    val abbreviation: String,
    val players: MutableList<Player>,
    val conferenceId: Int,// for use in games
    val isUser: Boolean,
    val coach: Coach
) {

    val roster = mutableListOf<Player>() // for use everywhere else
    private val userSubs = mutableListOf<Pair<Int, Int>>()
    var teamRating: Int = 0

    var twoPointAttempts = 0
    var twoPointMakes = 0
    var threePointAttempts = 0
    var threePointMakes = 0
    var offensiveRebounds = 0
    var defensiveRebounds = 0
    var turnovers = 0
    var offensiveFouls = 0
    var defensiveFouls = 0
    var freeThrowShots = 0
    var freeThrowMakes = 0

    var offenseFavorsThrees = coach.offenseFavorsThreesGame
    var defenseFavorsThrees = coach.defenseFavorsThreesGame
    var pressFrequency = coach.pressFrequencyGame
    var pressAggression = coach.pressAggressionGame
    var aggression = coach.aggressionGame
    var pace = coach.paceGame

    var userWantsTimeout = false
    var lastScoreDiff = 0

    val r = Random()

    init {
        roster.addAll(players)
        roster.sortBy { it.rosterIndex }
        teamRating = calculateTeamRating()
    }

    fun getPlayerAtPosition(position: Int): Player {
        // Converts position numbering convention 1-5 to indexing convention 0-4
        if (position in 1..5) {
            return players[position - 1]
        }

        throw Exception("There does not exist such a position!")
    }

    fun startGame() {
        twoPointAttempts = 0
        twoPointMakes = 0
        threePointAttempts = 0
        threePointMakes = 0
        offensiveRebounds = 0
        defensiveRebounds = 0
        turnovers = 0
        offensiveFouls = 0
        defensiveFouls = 0
        freeThrowShots = 0
        freeThrowMakes = 0

        lastScoreDiff = 0

        for (p in players) {
            p.startGame()
        }
        coach.startGame()

        players.sortBy { it.courtIndex }
    }

    fun pauseGame() {
        players.forEach {
            it.pauseGame()
            it.courtIndex = players.indexOf(it)
        }
    }

    fun resumeGame() {
        players.forEach { it.resumeGame() }
        players.sortBy { it.courtIndex }
    }

    fun updateTimePlayed(time: Int, isTimeout: Boolean, isHalftime: Boolean) {
        for (index in players.indices) {
            if (index < 5) {
                players[index].addTimePlayed(time, isTimeout, isHalftime)
            } else {
                players[index].addTimePlayed(0, isTimeout, isHalftime)
            }
        }
    }

    fun addFatigueFromPress() {
        for (index in 0..4) {
            players[index].addFatigueFromPress()
        }
    }

    fun endGame() {
        for (p in players) {
            p.inGame = false
        }
    }

    fun getStatsAsString(): String {
        return "$name\n" +
                "2FG:$twoPointMakes/$twoPointAttempts - ${twoPointMakes / (twoPointAttempts * 1.0)}\n" +
                "3FG:$threePointMakes/$threePointAttempts - ${threePointMakes / (threePointAttempts * 1.0)}\n" +
                "Rebounds:$offensiveRebounds/$defensiveRebounds - ${offensiveRebounds + defensiveRebounds}\n" +
                "TO:$turnovers\nOFouls:$offensiveFouls\n" +
                "DFouls:$defensiveFouls\n" +
                "FT:$freeThrowMakes/$freeThrowShots - ${freeThrowMakes / (freeThrowShots * 1.0)}"
    }

    fun coachTalk(homeTeam: Boolean, scoreDif: Int, talkType: CoachTalk) {
        val maxModifier = 30
        val gameVariability = 15
        val focusMod = 5
        val r = Random()

        for (p in players) {
            var gameMod = (r.nextDouble() * 2 * gameVariability) - gameVariability

            if (talkType == CoachTalk.CALM) {
                gameMod /= 2
            } else if (talkType == CoachTalk.AGGRESSIVE) {
                gameMod *= 2
            }

            if (homeTeam) {
                gameMod += 10
            }

            if (scoreDif > 10) {
                gameMod -= scoreDif - 10
            } else if (scoreDif < -10) {
                gameMod += -scoreDif - 10
            }

            when (talkType) {
                CoachTalk.OFFENSIVE -> {
                    p.offensiveStatMod = gameMod.toInt() + focusMod
                    p.defensiveStatMod = gameMod.toInt() - focusMod
                }
                CoachTalk.DEFENSIVE -> {
                    p.offensiveStatMod = gameMod.toInt() - focusMod
                    p.defensiveStatMod = gameMod.toInt() + focusMod
                }
                else -> {
                    p.offensiveStatMod = gameMod.toInt()
                    p.defensiveStatMod = gameMod.toInt()
                }
            }

            p.offensiveStatMod = if (p.offensiveStatMod > maxModifier) {
                maxModifier
            } else if (p.offensiveStatMod < -maxModifier) {
                -maxModifier
            } else {
                p.offensiveStatMod
            }

            p.defensiveStatMod = if (p.defensiveStatMod > maxModifier) {
                maxModifier
            } else if (p.defensiveStatMod < -maxModifier) {
                -maxModifier
            } else {
                p.defensiveStatMod
            }
        }
    }

    fun aiMakeSubs(freeThrowShooter: Int, half: Int, timeRemaining: Int) {
        for (index in 0..4) {
            if (index != freeThrowShooter) {
                val sub = getBestPlayerForPosition(index + 1)
                if (index != sub && (r.nextDouble() > .6 || players[index].isInFoulTrouble(half, timeRemaining))) {
                    // TODO: add coach's tendency to sub here
                    Collections.swap(players, index, sub)
                    players[index].courtIndex = index
                    players[sub].courtIndex = sub
                }
            }
        }

        players.forEach { it.courtIndex = players.indexOf(it) }
    }

    fun addPendingSub(sub: Pair<Int, Int>) {
        userSubs.add(sub)
    }

    fun makeUserSubs(freeThrowShooter: Int) {
        userSubs.filter { it.first != freeThrowShooter && it.second != freeThrowShooter }.forEach {
            Collections.swap(players, it.first, it.second)
        }
        userSubs.removeAll(userSubs.filter { it.first != freeThrowShooter && it.second != freeThrowShooter })
        players.forEach {
            it.courtIndex = players.indexOf(it)
            it.subPosition = it.courtIndex
        }
    }

    fun swapPlayers(player1: Int, player2: Int) {
        Collections.swap(roster, player1, player2)
        roster[player1].rosterIndex = player1
        roster[player2].rosterIndex = player2
    }

    fun coachWantsTimeout(scoreDif: Int): Boolean {
        return if (isUser) {
            userWantsTimeout
        } else {
            (Math.abs(scoreDif) < 25 && scoreDif - lastScoreDiff < -7 && r.nextBoolean())
        }
    }

    fun addNewPlayer(player: Player) {
        players.add(player)
        roster.add(player)
    }

    fun returningPlayers(returningPlayers: List<Player>) {
        players.clear()
        roster.clear()

        players.addAll(returningPlayers)
        roster.addAll(returningPlayers)
    }

    private fun getBestPlayerForPosition(position: Int): Int {
        var player = players[position - 1]
        var indexOfBest = position - 1
        for (index in players.indices) {
            if ((player.getRatingAtPositionNoFatigue(position) - player.fatigue) < (players[index].getRatingAtPositionNoFatigue(position) - players[index].fatigue)) {
                player = players[index]
                indexOfBest = index
            }
        }
        return indexOfBest
    }

    private fun calculateTeamRating(): Int {
        var rating = 0
        for (p in roster) {
            rating += p.getOverallRating()
        }
        return rating / roster.size
    }
}