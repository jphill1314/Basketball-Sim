package com.appdev.jphil.basketball.players

enum class PlayerType(val type: Int) {
    BALANCED(0),
    SHOOTER(1),
    DISTRIBUTOR(2),
    REBOUNDER(3),
    DEFENDER(4);

    companion object {
        val closeWeight =   doubleArrayOf(0.0,  0.2, -0.15, -0.1, -0.1)
        val midWeight =     doubleArrayOf(0.0,  0.2, -0.15, -0.1, -0.15)
        val longWeight =    doubleArrayOf(0.0,  0.2, -0.1,  -0.1, -0.15)
        val ftWeight =      doubleArrayOf(0.0,  0.3, -0.1,  -0.1, -0.1)
        val postOffWeight = doubleArrayOf(0.0,  0.0,  0.0,   0.1, -0.1)
        val ballWeight =    doubleArrayOf(0.0, -0.1,  0.2,  -0.1, -0.1)
        val passWeight =    doubleArrayOf(0.0, -0.1,  0.3,  -0.1, -0.1)
        val offMoveWeight = doubleArrayOf(0.0,  0.2,  0.0,  -0.1, -0.1)

        val postDefWeight =  doubleArrayOf(0.0, -0.25, 0.0, 0.1, 0.2)
        val perimDefWeight = doubleArrayOf(0.0, -0.25, 0.0, 0.0, 0.2)
        val onBallWeight =   doubleArrayOf(0.0, -0.1,  0.0, 0.1, 0.2)
        val offBallWeight =  doubleArrayOf(0.0, -0.1,  0.0, 0.1, 0.2)
        val stealWeight =    doubleArrayOf(0.0,  0.0,  0.0, 0.0, 0.1)
        val reboundWeight =  doubleArrayOf(0.0, -0.2,  0.0, 0.3, 0.0)

        val positionWeights = listOf(
            listOf(25, 45, 75, 85, 100), // pg
            listOf(25, 65, 75, 85, 100), // sg
            listOf(25, 45, 65, 80, 100), // sf
            listOf(25, 40, 50, 80, 100), // pf
            listOf(25, 35, 45, 75, 100)  // c
        )
    }
}