package plays

import Team

class Shot(homeTeamHasBall: Boolean, timeRemaining: Int, shotClock: Int, homeTeam: Team, awayTeam: Team, playerWithBall: Int, location: Int, val assisted: Boolean, val rushed: Boolean):
        BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location) {

    private var isFouled = false

    init{
        super.type = Plays.SHOT
        foul = Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.CLEAN)
        points = generatePlay()
    }

    override fun getPlayAsString(): String {
        return "${offense.getPlayerAtPosition(playerWithBall).lastName} takes a shot!"
    }

    override fun generatePlay(): Int {
        val shooter = offense.getPlayerAtPosition(playerWithBall)
        val defender = defense.getPlayerAtPosition(playerWithBall)

        // First determine where the shot will be taken from
        val shotClose = (shooter.closeRangeShot * (1 - (offense.offenseFavorsThrees / 100.0)) * (1 - (defense.defenseFavorsThrees / 100.0)) + r.nextInt(randomBound))
        val shotMid = (shooter.midRangeShot * .2 + r.nextInt(randomBound))
        val shotLong = (shooter.longRangeShot * (offense.offenseFavorsThrees / 100.0) * (defense.defenseFavorsThrees / 100.0) + r.nextInt(randomBound))


        val shotLocation: Int = if(location == 0){
            // shot taken from around half court
            offense.threePointAttempts++
            4
        }
        else if(location == -1){
            // shot taken from beyond half court
            offense.threePointAttempts++
            5
        }
        else if(shotClose > shotMid && shotClose > shotLong){
            // shot taken from close range
            offense.twoPointAttempts++
            1
        }
        else if(shotMid > shotClose && shotMid > shotLong){
            // shot taken from mid range
            offense.twoPointAttempts++
            2
        }
        else{
            // shot taken from 3
            offense.threePointAttempts++
            3
        }

        var shotSuccess: Int = when(shotLocation){
            1 -> (shooter.closeRangeShot - ((defender.onBallDefense + defender.postDefense) / 2.0) + r.nextInt(randomBound)).toInt()
            2 -> (shooter.midRangeShot - ((defender.onBallDefense + defender.postDefense + defender.perimeterDefense) / 3.0) + r.nextInt(randomBound)).toInt()
            3 -> (shooter.longRangeShot - ((defender.onBallDefense + defender.perimeterDefense) / 2.0) + r.nextInt(randomBound)).toInt()
            4 -> (shooter.longRangeShot - ((defender.onBallDefense + defender.perimeterDefense) / 2.0) + r.nextInt(randomBound) - 10).toInt()
            5 -> (shooter.longRangeShot - ((defender.onBallDefense + defender.perimeterDefense) / 2.0) + r.nextInt(randomBound) - 20).toInt()
            else -> 0
        }

        if(assisted){
            shotSuccess += 30
        }
        if(rushed){
            shotSuccess -= 30
        }

        foul = when(shotLocation){
            1 -> Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.SHOOTING_CLOSE)
            2 -> Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.SHOOTING_MID)
            else ->  Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.SHOOTING_LONG)
        }

        if(foul.foulType != FoulType.CLEAN){
            shotSuccess -= 30
        }

        // TODO: add chance to get fouled while shooting
        val timeChange = timeUtil.smartTimeChange((6 - (offense.pace / 90.0) * r.nextInt(4)).toInt(), shotClock)
        timeRemaining -= timeChange
        shotClock -= timeChange
        return if(shotSuccess > defense.aggression){
            if(shotLocation > 2 || r.nextBoolean()) {
                // shot made
                homeTeamHasBall = !homeTeamHasBall
                when (shotLocation) {
                    1 -> {
                        offense.twoPointMakes++; 2
                    }
                    2 -> {
                        offense.twoPointMakes++; 2
                    }
                    else -> {
                        offense.threePointMakes++; 3
                    }
                }
            }
            else{
                0
            }
        }
        else{
            // shot missed
            0
        }
    }
}