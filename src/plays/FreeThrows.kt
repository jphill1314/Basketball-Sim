package plays

import Team

class FreeThrows(homeTeamHasBall: Boolean, timeRemaining: Int, shotClock: Int, homeTeam: Team, awayTeam: Team, playerWithBall: Int, location: Int, val numberOfShots: Int):
        BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location) {

    var madeLastShot = true

    init{
        foul = Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.CLEAN)
        points = generatePlay()
    }

    override fun getPlayAsString(): String {
        return "Some free throws were shot!"
    }

    override fun generatePlay(): Int {
        var made = 0
        val shooter = offense.getPlayerAtPosition(playerWithBall)
        if(numberOfShots > 0) {
            for (i in 1..numberOfShots) {
                offense.freeThrowShots++
                if (r.nextInt(100) < shooter.freeThrowShot) {
                    made++
                    offense.freeThrowMakes++
                } else if (i == numberOfShots) {
                    madeLastShot = false
                }
            }
        }
        else{
            // 1 and 1 situation
            if (r.nextInt(100) < shooter.freeThrowShot) {
                made++
                offense.freeThrowShots++
                offense.freeThrowMakes++
                if (r.nextInt(100) < shooter.freeThrowShot) {
                    made++
                    offense.freeThrowShots++
                    offense.freeThrowMakes++
                }
                else {
                    offense.freeThrowShots++
                    madeLastShot = false
                }
            }
            else {
                offense.freeThrowShots++
                madeLastShot = false
            }
        }
        return made
    }
}