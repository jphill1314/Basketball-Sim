package com.appdev.jphil.basketballcoach.simulation

data class SimulationState(
    val isSimActive: Boolean = false,
    val isSimulatingToGame: Boolean = false,
    val numberOfGamesToSim: Int = 0,
    val numberOfGamesSimmed: Int = 0
)
