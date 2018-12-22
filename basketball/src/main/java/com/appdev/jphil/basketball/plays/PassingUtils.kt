package com.appdev.jphil.basketball.plays

import com.appdev.jphil.basketball.Team
import java.util.*

class PassingUtils(private val randomBound: Int) {

    private val r = Random()

    fun getTarget(team: Team, playerWithBall: Int): Int{
        val pg = if(playerWithBall == 1) 0 else team.getPlayerAtPosition(1).offBallMovement + r.nextInt(randomBound)
        val sg = if(playerWithBall == 2) 0 else team.getPlayerAtPosition(2).offBallMovement + r.nextInt(randomBound)
        val sf = if(playerWithBall == 3) 0 else team.getPlayerAtPosition(3).offBallMovement + r.nextInt(randomBound)
        val pf = if(playerWithBall == 4) 0 else team.getPlayerAtPosition(4).offBallMovement + r.nextInt(randomBound)
        val c = if(playerWithBall == 5) 0 else team.getPlayerAtPosition(5).offBallMovement + r.nextInt(randomBound)

        return if(c > pf && c > sf && c > sg && c > pg){
            5
        }
        else if(pf > c && pf > sf && pf > sg && pf > pg){
            4
        }
        else if(sf > c && sf > pf && sf > sg && sf > pg){
            3
        }
        else if(sg > c && sg > pf && sg > sf && sg > pg || playerWithBall == 1){
            2
        }
        else{
            1
        }
    }

    fun getInbounder(team: Team): Int{
        val pg = team.getPlayerAtPosition(1).passing + r.nextInt(randomBound)
        val sg = team.getPlayerAtPosition(2).passing + r.nextInt(randomBound)
        val sf = team.getPlayerAtPosition(3).passing + r.nextInt(randomBound)
        val pf = team.getPlayerAtPosition(4).passing + r.nextInt(randomBound)
        val c = team.getPlayerAtPosition(5).passing + r.nextInt(randomBound)

        return if(c > pf && c > sf && c > sg && c > pg){
            5
        }
        else if(pf > c && pf > sf && pf > sg && pf > pg){
            4
        }
        else if(sf > c && sf > pf && sf > sg && sf > pg){
            3
        }
        else if(sg > c && sg > pf && sg > sf && sg > pg){
            2
        }
        else{
            1
        }
    }
}