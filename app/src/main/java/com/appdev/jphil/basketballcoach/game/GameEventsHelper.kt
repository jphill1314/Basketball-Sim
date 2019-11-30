package com.appdev.jphil.basketballcoach.game

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.TipOff
import com.appdev.jphil.basketballcoach.database.game.GameEventEntity

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
                    newEvents.add(
                        GameEventEntity(
                            totalEvents++,
                            game.id!!,
                            play.timeRemaining,
                            play.shotClock,
                            play.playAsString,
                            game.homeTeam.abbreviation,
                            game.awayTeam.abbreviation,
                            game.homeScore,
                            game.awayScore,
                            if (play is TipOff) play.homeTeamHasBall else play.homeTeamStartsWithBall
                        )
                    )
                } else {
                    val last = newPlays[index - 1]
                    if (last.timeRemaining == play.timeRemaining) {
                        newEvents.last().event += "\n${play.playAsString}"
                    } else {
                        newEvents.add(
                            GameEventEntity(
                                totalEvents++,
                                game.id!!,
                                play.timeRemaining,
                                play.shotClock,
                                play.playAsString,
                                game.homeTeam.abbreviation,
                                game.awayTeam.abbreviation,
                                game.homeScore,
                                game.awayScore,
                                if (play is TipOff) play.homeTeamHasBall else play.homeTeamStartsWithBall
                            )
                        )
                    }
                }
            }
        }
//
//        lastPlay = plays.size
//        gameEvents.addAll(newEvents)
        return newEvents
    }
}