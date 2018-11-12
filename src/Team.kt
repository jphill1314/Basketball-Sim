import java.util.*
import kotlin.math.abs

class Team(val Name: String, rating: Int){

    val players = ArrayList<Player>()
    var offenseFavorsThrees: Int
    var defenseFavorsThrees: Int
    var aggression: Int
    var pace: Int

    var twoPointAttempts = 0
    var twoPointMakes = 0
    var threePointAttempts = 0
    var threePointMakes = 0
    var offensiveRebounds = 0
    var defensiveRebounds = 0
    var turnovers = 0
    var offensiveFouls = 0
    var defensiveFouls = 0
    var freeThrowShots = 0
    var freeThrowMakes = 0

    init{
        offenseFavorsThrees = 50
        defenseFavorsThrees = 50
        aggression = 0
        pace = 70

        for(i in 1..5){
            players.add(Player(Name, "$i", i, rating))
        }
    }

    fun getPlayerAtPosition(position: Int): Player{
        // Converts position numbering convention 1-5 to indexing convention 0-4
        if(position in 1..5){
            return players[position - 1]
        }

        throw Exception("There does not exist such a position!")
    }

    fun startGame(){
        twoPointAttempts = 0
        twoPointMakes = 0
        threePointAttempts = 0
        threePointMakes = 0
        offensiveRebounds = 0
        defensiveRebounds = 0
        turnovers = 0
        offensiveFouls = 0
        defensiveFouls = 0
        freeThrowShots = 0
        freeThrowMakes = 0

        for(p in players){
            p.inGame = true
            p.offensiveStatMod = 0
            p.defensiveStatMod = 0
        }
    }

    fun endGame(){
        for(p in players){
            p.inGame = false
            p.offensiveStatMod = 0
            p.defensiveStatMod = 0
        }
    }

    fun getStatsAsString(): String{
        return "$Name\n2FG:$twoPointMakes/$twoPointAttempts\n3FG:$threePointMakes/$threePointAttempts\n" +
                "Rebounds:$offensiveRebounds/$defensiveRebounds\nTO:$turnovers\nOFouls:$offensiveFouls\n" +
                "DFouls:$defensiveFouls\nFTA:$freeThrowShots\nFTM:$freeThrowMakes"
    }

    fun coachTalk(homeTeam: Boolean, scoreDif: Int, talkType: CoachTalk){
        val maxModifier = 30
        val gameVariability = 15
        val focusMod = 5
        val r = Random()

        for(p in players){
            var gameMod = (r.nextDouble() * 2 * gameVariability) - gameVariability

            if(talkType == CoachTalk.CALM){
                gameMod /= 2
            }
            else if(talkType == CoachTalk.AGGRESSIVE){
                gameMod *= 2
            }

            if(homeTeam){
                gameMod += 1
            }

            if(scoreDif > 10){
                gameMod -= scoreDif - 10
            }
            else if(scoreDif < -10){
                gameMod += -scoreDif - 10
            }

//            if(scoreDif in 11..15){
//                gameMod = -(scoreDif - 10.0)
//            }
//            else if(scoreDif in 16..20){
//                when(scoreDif){
//                    16 -> gameMod = -9.0
//                    17 -> gameMod = -12.0
//                    19 -> gameMod = -15.0
//                    20 -> gameMod = -18.0
//                }
//            }
//            else if(scoreDif > 20){
//                gameMod = -scoreDif.toDouble()
//            }
//            else if(scoreDif < -10 && gameMod < 0){
//                gameMod = 0.0
//            }

            when(talkType){
                CoachTalk.OFFENSIVE -> {
                    p.offensiveStatMod = gameMod.toInt() + focusMod
                    p.defensiveStatMod = gameMod.toInt() - focusMod
                }
                CoachTalk.DEFENSIVE -> {
                    p.offensiveStatMod = gameMod.toInt() - focusMod
                    p.defensiveStatMod = gameMod.toInt() + focusMod
                }
                else -> {
                    p.offensiveStatMod = gameMod.toInt()
                    p.defensiveStatMod = gameMod.toInt()
                }
            }

            p.offensiveStatMod = if(p.offensiveStatMod > maxModifier){
                maxModifier
            }
            else if(p.offensiveStatMod < -maxModifier){
                -maxModifier
            }
            else {
                p.offensiveStatMod
            }

            p.defensiveStatMod = if(p.defensiveStatMod > maxModifier){
                maxModifier
            }
            else if(p.defensiveStatMod < -maxModifier){
                -maxModifier
            }
            else {
                p.defensiveStatMod
            }
        }
    }

    fun getTeamRating(): Int{
        var rating = 0
        for(p in players){
            rating += p.getOverallRating()
        }
        return rating / players.size
    }
}