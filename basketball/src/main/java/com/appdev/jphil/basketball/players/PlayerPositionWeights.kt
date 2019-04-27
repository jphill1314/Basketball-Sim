package com.appdev.jphil.basketball.players

object PlayerPositionWeights {

    val closeWeight = doubleArrayOf(.6, .7, .8, 1.0, 1.0)
    val midWeight = doubleArrayOf(.8, .8, .8, .7, .7)
    val longWeight = doubleArrayOf(.8, 1.0, .8, .5, .5)
    val ftWeight = doubleArrayOf(.8, .8, .8, .6, .6)
    val postOffWeight = doubleArrayOf(.2, .2, .5, 1.3, 1.3)
    val ballWeight = doubleArrayOf(1.1, .8, .8, .6, .5)
    val passWeight = doubleArrayOf(1.1, .8, .8, .6, .5)
    val offMoveWeight = doubleArrayOf(.8, 1.0, .8, .7, .7)

    val postDefWeight = doubleArrayOf(.4, .5, .6, .9, 1.0)
    val perimDefWeight = doubleArrayOf(1.0, 1.0, .9, .5, .5)
    val onBallWeight = doubleArrayOf(1.0, .8, .8, .9, 1.0)
    val offBallWeight = doubleArrayOf(.8, .8, .8, .7, .6)
    val stealWeight = doubleArrayOf(.8, .8, .8, .5, .5)
    val reboundWeight = doubleArrayOf(.4, .5, .7, 1.2, 1.2)
}