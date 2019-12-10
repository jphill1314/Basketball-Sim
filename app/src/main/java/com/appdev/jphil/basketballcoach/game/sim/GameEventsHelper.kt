package com.appdev.jphil.basketballcoach.game.sim

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.BasketballPlay
import com.appdev.jphil.basketball.plays.EndOfHalf
import com.appdev.jphil.basketball.plays.TipOff
import com.appdev.jphil.basketballcoach.database.game.GameEventEntity
import com.appdev.jphil.basketballcoach.util.getColor

object GameEventsHelper {

    fun getNewPlayEvents(game: Game, lastPlay: Int, total: Int): List<GameEventEntity> {
        val newEvents = mutableListOf<GameEventEntity>()
        val plays = game.gamePlays
        var totalEvents = total

        if (plays.size > lastPlay) {
            val newPlays = plays.subList(lastPlay, plays.size)

            for (index in newPlays.indices) {
                val play = newPlays[index]
                if (index == 0) {
                    newEvents.add(createEvent(game, play, totalEvents++))
                } else {
                    val last = newPlays[index - 1]
                    if (last.timeRemaining == play.timeRemaining) {
                        newEvents.last().event += "\n${play.playAsString}"
                    } else {
                        newEvents.add(createEvent(game, play, totalEvents++))
                    }
                }
            }
        }
        return newEvents
    }

    private fun createEvent(game: Game, play: BasketballPlay, totalEvents: Int): GameEventEntity {
        val homeTeamHasBall = when (play) {
            is TipOff -> play.homeTeamHasBall
            is EndOfHalf -> play.homeTeamHadBall
            else -> play.homeTeamStartsWithBall
        }
        return GameEventEntity(
            totalEvents,
            game.id!!,
            play.timeRemaining,
            play.shotClock,
            play.playAsString,
            game.homeTeam.abbreviation,
            game.awayTeam.abbreviation,
            game.homeScore,
            game.awayScore,
            homeTeamHasBall
        )
    }
}