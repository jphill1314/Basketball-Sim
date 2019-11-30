package com.appdev.jphil.basketballcoach.game

import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketballcoach.database.game.GameEventEntity

class GameState(
    val game: Game,
    val newEvents: List<GameEventEntity>,
    val isNewHalf: Boolean = false
)