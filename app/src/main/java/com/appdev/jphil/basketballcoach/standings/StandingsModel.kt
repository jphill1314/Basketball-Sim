package com.appdev.jphil.basketballcoach.standings

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.conference.ConferenceEntity
import com.appdev.jphil.basketballcoach.database.game.GameEntity

data class StandingsModel(
    val teams: List<Team>,
    val games: List<GameEntity>,
    val conferences: List<ConferenceEntity>
)
