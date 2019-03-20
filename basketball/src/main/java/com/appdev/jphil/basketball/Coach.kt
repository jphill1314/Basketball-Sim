package com.appdev.jphil.basketball

class Coach(
    var id: Int?,
    val teamId: Int,
    val firstName: String,
    val lastName: String,
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
    var pressAggressionGame: Int
) {
    // TODO: give coaches a sub frequency rating that impacts when subs are made
    // TODO: add functions to change a team's strategy based on who is on the court and the score

    fun startGame() {
        paceGame = pace
        aggressionGame = aggression
        offenseFavorsThreesGame = offenseFavorsThrees
        defenseFavorsThreesGame = defenseFavorsThrees
        pressFrequencyGame = pressFrequency
        pressAggressionGame = pressAggression
    }

    companion object {
        const val minimumPace = 60
    }
}