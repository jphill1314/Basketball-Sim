
import plays.*
import java.util.*

class Game(val homeTeam: Team, val awayTeam: Team, val isNeutralCourt: Boolean) {
    private val lengthOfHalf = 20 * 60 // 20 minutes
    private val lengthOfOvertime = 5 * 60 // 5 minutes
    private val lengthOfShotClock = 30 // 30 seconds
    private val resetShotClockTime = 20 // shot clock resets to 20 on a defensive foul

    var shotClock = lengthOfShotClock
    var timeRemaining = lengthOfHalf
    var half = 1
    var homeScore = 0
    var awayScore = 0
    var homeFouls = 0
    var awayFouls = 0

    var mediaTimeOuts = Array(10, {i -> false})
    var homeTeamHasBall = true
    var deadball = false
    var madeShot = false
    var shootFreeThrows = false
    var numberOfFreeThrows = 0
    var playerWithBall = 1
    var location = 0

    val gamePlays = ArrayList<BasketballPlay>()

    private val r = Random()

    fun getAsString(): String{
        return "Half:$half \t ${homeTeam.Name}:$homeScore - ${awayTeam.Name}:$awayScore"
    }

    fun simulateFullGame(){
        homeTeam.startGame()
        awayTeam.startGame()

        while(half < 3 || homeScore == awayScore){
            //println("Half: $half Home: $homeScore  Away: $awayScore")
            timeRemaining = if(half < 3) lengthOfHalf else lengthOfOvertime
            shotClock = lengthOfShotClock

            if(half < 3){
                homeFouls = 0
                awayFouls = 0
            }

            while(timeRemaining > 0) {
                gamePlays.addAll(getNextPlay())
                if(deadball && !madeShot && getMediaTimeout()){
                    // TODO: use actual rules here
                    runTimeout()
                }
            }
            half++
        }
        half--
        //println("Final! Half: $half Home: $homeScore  Away: $awayScore")

        homeTeam.endGame()
        awayTeam.endGame()

        if(half > 4){
            println("wtf: $homeScore - $awayScore half:$half")
        }
    }

    private fun getNextPlay(): ArrayList<BasketballPlay>{
        val plays: ArrayList<BasketballPlay> = if(shootFreeThrows){
            getFreeThrows()
        }
        else{
            getGamePlay()
        }

        timeRemaining = plays[plays.size - 1].timeRemaining
        shotClock = plays[plays.size - 1].shotClock
        location = plays[plays.size - 1].location

        manageFouls(plays[plays.size - 1].foul)

        if(plays[plays.size - 1].homeTeamHasBall != this.homeTeamHasBall){
            changePossession()
        }

        if(shotClock == 0){
            if(homeTeamHasBall){
                homeTeam.turnovers++
            }
            else{
                awayTeam.turnovers++
            }
            changePossession()
        }

        return plays
    }

    private fun getFreeThrows(): ArrayList<BasketballPlay>{
        shootFreeThrows = false
        val plays = ArrayList<BasketballPlay>()
        val freeThrows = FreeThrows(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, numberOfFreeThrows)
        addPoints(freeThrows.points)
        plays.add(freeThrows)

        if(!freeThrows.madeLastShot){
            plays.add(Rebound(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location))
        }

        return plays
    }

    private fun getGamePlay(): ArrayList<BasketballPlay>{
        val shotUrgency: Int = if(homeTeamHasBall) lengthOfHalf / homeTeam.pace else lengthOfHalf / awayTeam.pace
        val plays = ArrayList<BasketballPlay>()

        madeShot = false
        if(((shotClock < shotUrgency || r.nextDouble() > 0.7) && location == 1) || (shotClock <= 5 && r.nextDouble() > 0.05)){
            plays.add(Shot(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, false, deadball))
            if(plays[0].points == 0 && plays[0].foul.foulType == FoulType.CLEAN){
                // missed shot need to get a rebound
                plays.add(Rebound(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location))
                deadball = false
            }
            else if(plays[0].foul.foulType != FoulType.CLEAN){
                // shoot free throws now?
                shootFreeThrows = true
                if(plays[0].points != 0){
                    addPoints(plays[0].points)
                    numberOfFreeThrows = 1
                }
                else if(plays[0].foul.foulType == FoulType.SHOOTING_LONG){
                    numberOfFreeThrows = 3
                }
                else{
                    numberOfFreeThrows = 2
                }

                deadball = true
            }
            else{
                // made shot
                madeShot = true
                deadball = true
                if(homeTeamHasBall){
                    homeScore += plays[0].points
                }
                else{
                    awayScore += plays[0].points
                }
            }
        }
        else{
            plays.add(Pass(homeTeamHasBall, timeRemaining, shotClock, homeTeam, awayTeam, playerWithBall, location, false))
            deadball = false
        }

        return plays
    }

    private fun manageFouls(foul: Foul){
        if(foul.foulType != FoulType.CLEAN) {
            deadball = true
            madeShot = false // allow media timeouts to be called

            if(foul.isOnDefense){
                if(homeTeamHasBall){
                    awayFouls++
                }
                else{
                    homeFouls++
                }
            }
            else{
                if(homeTeamHasBall){
                    homeFouls++
                }
                else{
                    awayFouls++
                }
            }

            if(foul.isOnDefense){
                changePossession()
            }
            else{
                if(homeTeamHasBall && awayFouls > 6){
                    shootFreeThrows = true
                    numberOfFreeThrows = if(awayFouls >= 10){
                        2
                    }
                    else{
                        -1
                    }
                }
                else if(homeFouls > 6){
                    shootFreeThrows = true
                    numberOfFreeThrows = if(homeFouls >= 10){
                        2
                    }
                    else{
                        -1
                    }
                }

                if(shotClock < resetShotClockTime){
                    shotClock = resetShotClockTime
                }
            }
        }
    }

    private fun addPoints(points: Int){
        if(homeTeamHasBall){
            homeScore += points
        }
        else{
            awayScore += points
        }
    }

    private fun getMediaTimeout(): Boolean{
        return if(half == 1){
            if(timeRemaining < 16 * 60 && !mediaTimeOuts[0]){
                mediaTimeOuts[0] = true
                true
            }
            else if(timeRemaining < 12 * 60 && !mediaTimeOuts[1]){
                mediaTimeOuts[1] = true
                true
            }
            else if(timeRemaining < 8 * 60 && !mediaTimeOuts[2]){
                mediaTimeOuts[2] = true
                true
            }
            else if(timeRemaining < 4 * 60 && !mediaTimeOuts[3]){
                mediaTimeOuts[3] = true
                true
            }
            else{
                false
            }
        }
        else if(half == 2){
            if(timeRemaining < 16 * 60 && !mediaTimeOuts[4]){
                mediaTimeOuts[4] = true
                true
            }
            else if(timeRemaining < 12 * 60 && !mediaTimeOuts[5]){
                mediaTimeOuts[5] = true
                true
            }
            else if(timeRemaining < 8 * 60 && !mediaTimeOuts[6]){
                mediaTimeOuts[6] = true
                true
            }
            else if(timeRemaining < 4 * 60 && !mediaTimeOuts[7]){
                mediaTimeOuts[7] = true
                true
            }
            else{
                false
            }
        }
        else{
            false
        }
    }

    private fun runTimeout(){
        homeTeam.coachTalk(!isNeutralCourt, homeScore - awayScore, CoachTalk.NEUTRAL)
        awayTeam.coachTalk(false, awayScore - homeScore, CoachTalk.NEUTRAL)
    }

    private fun changePossession(){
        shotClock = lengthOfShotClock
        homeTeamHasBall = !homeTeamHasBall
        if(location != 0){
            location = -location
        }
    }
}