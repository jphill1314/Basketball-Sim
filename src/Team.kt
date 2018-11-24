import java.util.*

class Team(val Name: String, rating: Int){

    var players = ArrayList<Player>()
    val roster = ArrayList<Player>()
    var offenseFavorsThrees: Int
    var defenseFavorsThrees: Int
    var aggression: Int
    var pace: Int
    var teamRating: Int = 0

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

    val r = Random()

    init{
        offenseFavorsThrees = 50
        defenseFavorsThrees = 50
        aggression = 0
        pace = 70

        for(i in 1..5){
            roster.add(Player(Name, "$i", i, rating+10))
        }
        for(i in 1..5){
            roster.add(Player(Name, "${i+5}", i, rating))
        }
        for(i in 1..r.nextInt(6)+1){
            roster.add(Player(Name, "${i+10}", r.nextInt(5) + 1, rating-5))
        }

        teamRating = calculateTeamRating()
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
            p.fatigue = 0.0
            p.timePlayed = 0
        }

        players = ArrayList(roster)
    }

    fun updateTimePlayed(time: Int, isTimeout: Boolean, isHalftime: Boolean){
        for(index in players.indices){
            if(index < 5){
                players[index].addTimePlayed(time, isTimeout, isHalftime)
            }
            else{
                players[index].addTimePlayed(0, isTimeout, isHalftime)
            }
        }
    }

    fun endGame(){
        for(p in players){
            p.inGame = false
        }
    }

    fun getStatsAsString(): String{
        return "$Name\n" +
                "2FG:$twoPointMakes/$twoPointAttempts - ${twoPointMakes/(twoPointAttempts*1.0)}\n" +
                "3FG:$threePointMakes/$threePointAttempts - ${threePointMakes/(threePointAttempts*1.0)}\n" +
                "Rebounds:$offensiveRebounds/$defensiveRebounds - ${offensiveRebounds+defensiveRebounds}\n" +
                "TO:$turnovers\nOFouls:$offensiveFouls\n" +
                "DFouls:$defensiveFouls\n" +
                "FT:$freeThrowMakes/$freeThrowShots - ${freeThrowMakes/(freeThrowShots*1.0)}"
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
                gameMod += 10
            }

            if(scoreDif > 10){
                gameMod -= scoreDif - 10
            }
            else if(scoreDif < -10){
                gameMod += -scoreDif - 10
            }

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

    fun aiMakeSubs(){
        for(index in 0..4){
            val sub = getBestPlayerForPosition(index + 1)
            if(index != sub && r.nextDouble() > .6){
                // TODO: add coach's tendency to sub here
                Collections.swap(players, index, sub)
            }
        }
    }

    private fun getBestPlayerForPosition(position: Int): Int{
        var player = players[position-1]
        var indexOfBest = position - 1
        for(index in players.indices){
            if((player.getRatingAtPosition(position) - player.fatigue) < (players[index].getRatingAtPosition(position) - players[index].fatigue)){
                player = players[index]
                indexOfBest = index
            }
        }
        return indexOfBest
    }

    private fun calculateTeamRating(): Int{
        var rating = 0
        for(p in roster){
            rating += p.getOverallRating()
        }
        return rating / roster.size
    }
}