package plays

import Team

class Rebound(homeTeamHasBall: Boolean, timeRemaining: Int, shotClock: Int, homeTeam: Team, awayTeam: Team, playerWithBall: Int, location: Int):
        BasketballPlay(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location) {

    init{
        points = generatePlay()
    }

    override fun getPlayAsString(): String {
        return if(homeTeamHasBall){
            "${homeTeam.getPlayerAtPosition(playerWithBall).lastName} grabs the rebound."
        }
        else{
            "${awayTeam.getPlayerAtPosition(playerWithBall).lastName} grabs the rebound."
        }
    }

    override fun generatePlay(): Int {
        val offChance = getReboundChance(offense)
        val defChance = getReboundChance(defense)

        offChance[playerWithBall - 1] -= 15 // less likely to get own rebound
        var offHigh = 0
        var defHigh = 0

        for(i in 1..4){
            if(offChance[i] > offChance[offHigh]){
                offHigh = i
            }

            if(defChance[i] > defChance[defHigh]){
                defHigh = i
            }
        }

        if(offChance[offHigh] - 30 > defChance[defHigh]){
            // offensive rebound
            playerWithBall = offHigh + 1 // convert index to basketball position
            foul = Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.REBOUNDING)
            if(foul.foulType == FoulType.CLEAN) {
                offense.offensiveRebounds++
            }
        }
        else{
            // defensive rebound
            playerWithBall = defHigh + 1 // convert index to basketball position
            homeTeamHasBall = !homeTeamHasBall
            location = -1
            foul = Foul(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, FoulType.REBOUNDING)
            if(foul.foulType == FoulType.CLEAN) {
                defense.defensiveRebounds++
            }
            else{
                homeTeamHasBall = !homeTeamHasBall
                location = 1
            }
        }

        return 0
    }

    private fun getReboundChance(team: Team): ArrayList<Int>{
        val values = ArrayList<Int>()
        for(i in 0..4){
            val player = team.players[i]
            values.add(player.rebounding + player.aggressiveness / 5 + r.nextInt(4 * randomBound))
        }
        return values
    }
}