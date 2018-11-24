package plays

import Team
import Player

class Pass(homeTeamHasBall: Boolean, timeRemaining: Int, shotClock: Int, homeTeam: Team, awayTeam: Team, playerWithBall: Int, location: Int, val deadBall: Boolean):
        BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location) {

    private var playerStartsWithBall = playerWithBall
    private lateinit var passer: Player
    private lateinit var passDefender: Player
    private lateinit var target: Player
    private lateinit var targetDefender: Player

    private var timeChange = 0

    init{
        super.type = Plays.PASS
        foul = Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.CLEAN)
        points = generatePlay()
    }

    override fun getPlayAsString(): String {
        return if(homeTeamHasBall) {
            "A pass from ${homeTeam.getPlayerAtPosition(playerStartsWithBall).lastName} to ${homeTeam.getPlayerAtPosition(playerWithBall).lastName}."
        }
        else{
            "A pass from ${awayTeam.getPlayerAtPosition(playerStartsWithBall).lastName} to ${awayTeam.getPlayerAtPosition(playerWithBall).lastName}."
        }
    }

    override fun generatePlay(): Int {
        if(deadBall){
            playerWithBall = getInbounder(offense)
            playerStartsWithBall = playerWithBall
        }

        passer = offense.getPlayerAtPosition(playerWithBall)
        passDefender = defense.getPlayerAtPosition(playerWithBall)

        val targetPos: Int = getTarget(offense)
        target = offense.getPlayerAtPosition(targetPos)
        targetDefender = defense.getPlayerAtPosition(targetPos)

        val passSuccess = (passer.passing + target.offBallMovement) / (r.nextInt(randomBound) + 1)
        val stealSuccess = ((passDefender.onBallDefense + passDefender.stealing + targetDefender.offBallDefense + targetDefender.stealing) / 2) / (r.nextInt(super.randomBound) + 1)

        //println("pass success: $passSuccess vs. pass fail:${(defense.aggression + (passDefender.aggressiveness + targetDefender.aggressiveness) / 15)}")
        if(passSuccess >= (defense.aggression + (passDefender.aggressiveness + targetDefender.aggressiveness) / 15)){
            // successful pass
            //TODO: add chance to have the ball knocked out of bounds
            playerWithBall = targetPos
            timeChange = if(location < 1){
                // the ball was inbounded in the backcourt so more time needs to come off the clock
                location = 1
                timeUtil.smartTimeChange(((9 - (offense.pace / 90.0)) * r.nextInt(6)).toInt(), shotClock)
            }
            else{
                //TODO: add pass leading to a shot / post move / etc
                timeUtil.smartTimeChange(((8 - (offense.pace / 90.0)) * r.nextInt(4)).toInt(), shotClock)
            }
        }
        else if(passSuccess < ((defense.aggression + (passDefender.aggressiveness + targetDefender.aggressiveness) / 15)) - 6){
            // Bad pass -> turnover
            /*if(r.nextInt(100) > 60){
                // target of pass is at fault
            }
            else{
                // passer is at fault
            }*/
            offense.turnovers++
            timeChange = timeUtil.smartTimeChange((4 - (offense.pace / 90.0) * r.nextInt(3)).toInt(), shotClock)
            homeTeamHasBall = !homeTeamHasBall
        }
        else if(stealSuccess > (100 - defense.aggression - ((passDefender.aggressiveness + targetDefender.aggressiveness) / 2))){
            // Pass is stolen by defense
            if(r.nextBoolean()){
                // ball is stolen by defender of target
                playerWithBall = targetPos
                foul = Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.ON_BALL)
            }
            else{
                // ball is stolen by defender of passer
                foul = Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.ON_BALL)
            }
            timeChange = timeUtil.smartTimeChange((4 - (offense.pace / 90.0) * r.nextInt(3)).toInt(), shotClock)

            if(foul.foulType == FoulType.CLEAN) {
                offense.turnovers++
                homeTeamHasBall = !homeTeamHasBall
            }
        }
        else{
            // no pass takes place, just run off some time
            foul = if(r.nextBoolean()) {
                 Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.OFF_BALL)
            }
            else{
                Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.ON_BALL)
            }

            if(foul.foulType != FoulType.CLEAN){
                playerStartsWithBall = foul.positionOfPlayerFouled
                type = Plays.FOUL
            }
            else{
                type = Plays.DRIBBLE
            }

            timeChange = timeUtil.smartTimeChange((4 - (offense.pace / 90.0) * r.nextInt(3)).toInt(), shotClock)
            location = 1
        }

        timeRemaining -= timeChange
        shotClock -= timeChange
        return 0
    }

    private fun getTarget(team: Team): Int{
        val pg = if(playerWithBall == 1) 0 else team.getPlayerAtPosition(1).offBallMovement + r.nextInt(super.randomBound)
        val sg = if(playerWithBall == 2) 0 else team.getPlayerAtPosition(2).offBallMovement + r.nextInt(super.randomBound)
        val sf = if(playerWithBall == 3) 0 else team.getPlayerAtPosition(3).offBallMovement + r.nextInt(super.randomBound)
        val pf = if(playerWithBall == 4) 0 else team.getPlayerAtPosition(4).offBallMovement + r.nextInt(super.randomBound)
        val c = if(playerWithBall == 5) 0 else team.getPlayerAtPosition(5).offBallMovement + r.nextInt(super.randomBound)

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

    private fun getInbounder(team: Team): Int{
        val pg = team.getPlayerAtPosition(1).passing + r.nextInt(super.randomBound)
        val sg = team.getPlayerAtPosition(2).passing + r.nextInt(super.randomBound)
        val sf = team.getPlayerAtPosition(3).passing + r.nextInt(super.randomBound)
        val pf = team.getPlayerAtPosition(4).passing + r.nextInt(super.randomBound)
        val c = team.getPlayerAtPosition(5).passing + r.nextInt(super.randomBound)

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