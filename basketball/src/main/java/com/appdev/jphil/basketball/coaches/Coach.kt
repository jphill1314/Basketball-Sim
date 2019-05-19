package com.appdev.jphil.basketball.coaches

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
    var teachShooting: Int,
    var teachPostMoves: Int,
    var teachBallControl: Int,
    var teachPostDefense: Int,
    var teachPerimeterDefense: Int,
    var teachPositioning: Int,
    var teachRebounding: Int,
    var teachConditioning: Int,
    var scoutingAssignment: ScoutingAssignment
) {
    // TODO: give coaches a sub frequency rating that impacts when subs are made
    // TODO: add functions to change a team's strategy based on who is on the court and the score
    val fullName = "$firstName $lastName"

    fun startGame() {
        paceGame = pace
        aggressionGame = aggression
        offenseFavorsThreesGame = offenseFavorsThrees
        defenseFavorsThreesGame = defenseFavorsThrees
        pressFrequencyGame = pressFrequency
        pressAggressionGame = pressAggression
    }

    fun getRating(): Int {
        return (teachShooting + teachPostMoves + teachBallControl + teachPostDefense + teachPerimeterDefense +
                teachPositioning + teachRebounding + teachConditioning) / 8
    }

    fun doScouting(unknownRecruits: MutableList<Recruit>): List<Recruit> {
        return scoutingAssignment.doScouting(recruiting, unknownRecruits)
    }

    companion object {
        const val minimumPace = 60
    }
}