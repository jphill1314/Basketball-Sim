package com.appdev.jphil.basketball

class Coach(
    var id: Int?,
    val teamId: Int,
    val firstName: String,
    val lastName: String,
    val offenseFavorsThrees: Int,
    val pace: Int,
    val aggression: Int,
    val defenseFavorsThrees: Int,
    val pressFrequency: Int,
    val pressAggression: Int, // Above represents the coach's normal strat
    var offenseFavorsThreesGame: Int, // Below represents the coach's current strat
    var paceGame: Int,
    var aggressionGame: Int,
    var defenseFavorsThreesGame: Int,
    var pressFrequencyGame: Int,
    var pressAggressionGame: Int
) {
    // TODO: give coaches a sub frequency rating that impacts when subs are made
    // TODO: add functions to change a team's strategy based on who is on the court and the score

    companion object {
        const val minimumPace = 60
    }
}