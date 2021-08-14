package com.appdev.jphil.basketball.teams

import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.coaches.CoachType
import com.appdev.jphil.basketball.coaches.ScoutingAssignmentHelper
import com.appdev.jphil.basketball.game.CoachTalk
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.players.PracticeType
import com.appdev.jphil.basketball.recruits.Recruit
import java.util.Collections
import kotlin.math.abs
import kotlin.random.Random

class Team(
    val teamId: Int,
    val schoolName: String,
    val mascot: String,
    val abbreviation: String,
    val color: TeamColor,
    val players: MutableList<Player>, // for use in games
    val conferenceId: Int,
    val isUser: Boolean,
    val coaches: MutableList<Coach>,
    val knownRecruits: MutableList<Recruit>,
    var gamesPlayed: Int
) {

    val name = "$schoolName $mascot"
    val roster = mutableListOf<Player>() // for use everywhere else
    private val userSubs = mutableListOf<Pair<Int, Int>>()
    var teamRating: Int = 0

    var practiceType = PracticeType.NO_FOCUS

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

    var offenseFavorsThrees: Int
    var defenseFavorsThrees: Int
    var pressFrequency: Int
    var pressAggression: Int
    var aggression: Int
    var pace: Int
    var intentionallyFoul = false

    var userWantsTimeout = false
    var lastScoreDiff = 0

    init {
        roster.addAll(players)
        roster.sortBy { it.rosterIndex }
        teamRating = calculateTeamRating()

        val hc = getHeadCoach()
        offenseFavorsThrees = hc.offenseFavorsThreesGame
        defenseFavorsThrees = hc.defenseFavorsThreesGame
        pressFrequency = hc.pressFrequencyGame
        pressAggression = hc.pressAggressionGame
        aggression = hc.aggressionGame
        pace = hc.paceGame
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
        getHeadCoach().startGame()
        updateStrategy(0, 0, -1, 0)

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

    fun updateStrategy(teamScore: Int, opponentScore: Int, half: Int, timeRemaining: Int) {
        // TODO: have AI coaches actually update their strategy
        val hc = getHeadCoach()
        if (half > 0) {
            hc.updateStrategy(teamScore, opponentScore, half, timeRemaining)
        }
        offenseFavorsThrees = hc.offenseFavorsThreesGame
        defenseFavorsThrees = hc.defenseFavorsThreesGame
        pressFrequency = hc.pressFrequencyGame
        pressAggression = hc.pressAggressionGame
        aggression = hc.aggressionGame
        pace = hc.paceGame
        intentionallyFoul = hc.intentionallyFoul
    }

    fun updateTimePlayed(time: Int, isTimeout: Boolean, isHalftime: Boolean) {
        val isHurrying = getHeadCoach().shouldHurry
        for (index in players.indices) {
            if (index < 5) {
                players[index].addTimePlayed(time, isHurrying, isTimeout, isHalftime)
            } else {
                players[index].addTimePlayed(0, isHurrying, isTimeout, isHalftime)
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
            p.runPractice(practiceType, coaches)
        }
        gamesPlayed++
    }

    fun coachTalk(homeTeam: Boolean, scoreDif: Int, isUserCoaching: Boolean): Int {
        val maxModifier = 30
        val gameVariability = 15
        val focusMod = 5
        val homeCourtAdvantage = 4
        val talkType = getHeadCoach().getTeamTalk(isUserCoaching && isUser)
        var netReaction = 0

        for (p in players) {
            var gameMod = (Random.nextDouble() * 2 * gameVariability) - gameVariability

            if (talkType == CoachTalk.CALM) {
                gameMod /= 2
            } else if (talkType == CoachTalk.AGGRESSIVE) {
                gameMod *= 2
            }

            if (homeTeam) {
                gameMod += homeCourtAdvantage
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
            netReaction += ((p.offensiveStatMod + p.defensiveStatMod) / 2.0).toInt()
        }
        return netReaction
    }

    fun allPlayersAreEligible(): Boolean {
        for (i in 0..4) {
            if (!players[i].isEligible()) {
                return false
            }
        }
        return true
    }

    fun aiMakeSubs(freeThrowShooter: Int, half: Int, timeRemaining: Int) {
        for (index in 0..4) {
            if (index != freeThrowShooter) {
                val sub = getBestPlayerForPosition(index + 1)
                if (index != sub && (Random.nextDouble() > .6 || players[index].isInFoulTrouble(half, timeRemaining))) {
                    // TODO: add coach's tendency to sub here
                    Collections.swap(players, index, sub)
                    players[index].courtIndex = index
                    players[sub].courtIndex = sub
                }

                if (!players[index].isEligible()) {
                    val forceSub = getSubForIneligiblePlayer(index + 1)
                    Collections.swap(players, index, forceSub)
                    players[index].courtIndex = index
                    players[forceSub].courtIndex = forceSub
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
            (abs(scoreDif) < 25 && scoreDif - lastScoreDiff < -7 && Random.nextBoolean())
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
            if ((player.getRatingAtPositionNoFatigue(position) - player.fatigue) <
                (players[index].getRatingAtPositionNoFatigue(position) - players[index].fatigue) &&
                players[index].isEligible()
            ) {
                player = players[index]
                indexOfBest = index
            }
        }
        return indexOfBest
    }

    private fun getSubForIneligiblePlayer(position: Int): Int {
        var player = players[5]
        var indexOfBest = 5
        for (index in 5 until players.size) {
            if (!player.isEligible() || player.getRatingAtPosition(position) < players[index].getRatingAtPosition(position)) {
                indexOfBest = index
                player = players[index]
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

    fun getHeadCoach(): Coach = coaches.first { it.type == CoachType.HEAD_COACH }

    fun doScouting(allRecruits: List<Recruit>) {
        val unknownRecruits = mutableListOf<Recruit>()
        unknownRecruits.addAll(allRecruits)
        knownRecruits.forEach {
            unknownRecruits.remove(it)
        }
        coaches.filter { it.type != CoachType.HEAD_COACH }.forEach { coach ->
            if (!isUser) {
                ScoutingAssignmentHelper.updateScoutingAssignment(this, coach.scoutingAssignment)
            }
            coach.doScouting(unknownRecruits).forEach { recruit ->
                recruit.generateInitialInterest(this, coach.recruiting)
                unknownRecruits.remove(recruit)
                knownRecruits.add(recruit)
            }
        }
    }

    fun hasNeedAtPosition(position: Int): Boolean {
        var nextYearsPlayers = roster.filter { it.position == position && it.year != 3 }.size
        nextYearsPlayers += knownRecruits.filter { it.isCommitted && it.teamCommittedTo == teamId && it.position == position }.size
        return 3 - nextYearsPlayers > 0
    }
}
