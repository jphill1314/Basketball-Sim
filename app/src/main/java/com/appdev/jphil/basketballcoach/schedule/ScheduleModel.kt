package com.appdev.jphil.basketballcoach.schedule

import com.appdev.jphil.basketballcoach.database.game.GameEntity

data class ScheduleModel(
    val games: List<GameEntity>,
    val isUsersSchedule: Boolean
)
