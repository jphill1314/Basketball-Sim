package com.appdev.jphil.basketball

import java.util.*

class Team(val teamId: Int, val Name: String, rating: Int, generatePlayers: Boolean) {

    var players = mutableListOf<Player>() // for use in games
    val roster = mutableListOf<Player>() // for use everywhere else
    var offenseFavorsThrees: Int = 0
    var defenseFavorsThrees: Int = 0
    var pressFrequency: Int = 0
    var pressAggression: Int = 0
    var aggression: Int = 0
    var pace: Int = 0
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

    val r = Random()

    constructor(
        teamId: Int,
        name: String,
        offenseFavorsThrees: Int,
        defenseFavorsThrees: Int,
        pressFrequency: Int,
        pressAggression: Int,
        aggression: Int,
        pace: Int,
        players: List<Player>
    ) : this(teamId, name, 0, false) {
        this.offenseFavorsThrees = offenseFavorsThrees
        this.defenseFavorsThrees = defenseFavorsThrees
        this.pressFrequency = pressFrequency
        this.pressAggression = pressAggression
        this.aggression = aggression
        this.pace = pace
        roster.addAll(players)
        teamRating = calculateTeamRating()
    }

    init {
        if (generatePlayers) {
            offenseFavorsThrees = 50
            defenseFavorsThrees = 50
            pressFrequency = 50
            pressAggression = 50
            aggression = 0
            pace = 70

            for (i in 1..5) {
                roster.add(Player(Name, "$i", i, teamId, true, rating + 10))
            }
            for (i in 1..5) {
                roster.add(Player(Name, "${i + 5}", i, teamId, true, rating))
            }
            for (i in 1..r.nextInt(6) + 1) {
                roster.add(Player(Name, "${i + 10}", r.nextInt(5) + 1, teamId, true, rating - 5))
            }
            teamRating = calculateTeamRating()
        }
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

        for (p in players) {
            p.startGame()
        }

        players = mutableListOf()
        players.addAll(roster)
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
        return "$Name\n" +
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
                }
            }
        }
    }

    private fun getBestPlayerForPosition(position: Int): Int {
        var player = players[position - 1]
        var indexOfBest = position - 1
        for (index in players.indices) {
            if ((player.getRatingAtPosition(position) - player.fatigue) < (players[index].getRatingAtPosition(position) - players[index].fatigue)) {
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