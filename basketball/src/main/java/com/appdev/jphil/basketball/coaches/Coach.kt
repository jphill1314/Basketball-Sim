package com.appdev.jphil.basketball.coaches

import com.appdev.jphil.basketball.game.CoachTalk
import com.appdev.jphil.basketball.recruits.Recruit

class Coach(
    var id: Int?,
    val teamId: Int,
    var type: CoachType,
    val firstName: String,
    val lastName: String,
    var recruiting: Int,
    var offenseFavorsThrees: Int,
    var pace: Int,
    var aggression: Int,
    var defenseFavorsThrees: Int,
    var pressFrequency: Int,
    var pressAggression: Int, // Above represents the coach's normal strat
    var offenseFavorsThreesGame: Int, // Below represents the coach's current strat
    var paceGame: Int,
    var aggressionGame: Int,
    var defenseFavorsThreesGame: Int,
    var pressFrequencyGame: Int,
    var pressAggressionGame: Int,
    var intentionallyFoul: Boolean,
    var shouldHurry: Boolean,
    var shouldWasteTime: Boolean,
    var teachShooting: Int,
    var teachPostMoves: Int,
    var teachBallControl: Int,
    var teachPostDefense: Int,
    var teachPerimeterDefense: Int,
    var teachPositioning: Int,
    var teachRebounding: Int,
    var teachConditioning: Int,
    val recruitingAssignments: MutableList<Recruit>
) {
    // TODO: give coaches a sub frequency rating that impacts when subs are made
    // TODO: add functions to change a team's strategy based on who is on the court and the score
    val fullName = "$firstName $lastName"

    var teamTalkType = CoachTalk.NEUTRAL

    fun startGame() {
        paceGame = pace
        aggressionGame = aggression
        offenseFavorsThreesGame = offenseFavorsThrees
        defenseFavorsThreesGame = defenseFavorsThrees
        pressFrequencyGame = pressFrequency
        pressAggressionGame = pressAggression
        intentionallyFoul = false
        shouldHurry = false
        shouldWasteTime = false
    }

    fun getRating(): Int {
        return (
            teachShooting + teachPostMoves + teachBallControl + teachPostDefense + teachPerimeterDefense +
                teachPositioning + teachRebounding + teachConditioning
            ) / 8
    }

    fun updateStrategy(teamScore: Int, opponentScore: Int, half: Int, timeRemaining: Int) {
        // TODO: change more than intentionally foul and make coaches actually different as to when to call this
        if (timeRemaining < 2 * 60 && half > 1) {
            StrategyHelper.updateStrategyLateGame(teamScore - opponentScore, timeRemaining, this)
        } else {
            StrategyHelper.updateStrategy(teamScore - opponentScore, this)
        }
    }

    fun getTeamTalk(useUserTalk: Boolean) = if (useUserTalk) {
        teamTalkType
    } else {
        // TODO: have AI pick talks
        CoachTalk.NEUTRAL
    }

    companion object {
        const val minimumPace = 60
    }
}
