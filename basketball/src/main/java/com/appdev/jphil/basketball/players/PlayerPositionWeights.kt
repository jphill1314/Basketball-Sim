package com.appdev.jphil.basketball.players

object PlayerPositionWeights {

    val closeWeight =   doubleArrayOf(0.5, 0.6, 0.7, 0.9, 0.9)
    val midWeight =     doubleArrayOf(0.7, 0.7, 0.7, 0.6, 0.6)
    val longWeight =    doubleArrayOf(0.7, 0.9, 0.7, 0.4, 0.4)
    val ftWeight =      doubleArrayOf(0.7, 0.7, 0.7, 0.5, 0.5)
    val postOffWeight = doubleArrayOf(0.1, 0.1, 0.4, 1.2, 1.2)
    val ballWeight =    doubleArrayOf(1.0, 0.7, 0.7, 0.5, 0.4)
    val passWeight =    doubleArrayOf(1.0, 0.7, 0.7, 0.5, 0.4)
    val offMoveWeight = doubleArrayOf(0.7, 0.9, 0.7, 0.6, 0.6)

    val postDefWeight =  doubleArrayOf(0.3, 0.4, 0.5, 0.8, 0.9)
    val perimDefWeight = doubleArrayOf(0.9, 0.9, 0.8, 0.4, 0.4)
    val onBallWeight =   doubleArrayOf(0.9, 0.7, 0.7, 0.8, 0.9)
    val offBallWeight =  doubleArrayOf(0.7, 0.7, 0.7, 0.6, 0.5)
    val stealWeight =    doubleArrayOf(0.7, 0.7, 0.7, 0.4, 0.4)
    val reboundWeight =  doubleArrayOf(0.5, 0.4, 0.6, 1.1, 1.1)
}