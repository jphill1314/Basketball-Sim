package com.appdev.jphil.basketball

import com.appdev.jphil.basketball.conference.Conference
import com.appdev.jphil.basketball.factories.PlayerFactory
import com.appdev.jphil.basketball.factories.RecruitFactory
import com.appdev.jphil.basketball.factories.TeamFactory
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.players.PracticeType
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.schedule.NonConferenceScheduleGen
import com.appdev.jphil.basketball.schedule.smartShuffleList
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.teams.TeamColor
import com.appdev.jphil.basketball.teams.TeamRecruitInteractor
import kotlin.random.Random


fun main() {
    simulateXSeasons(20)
//    distributionTest()
}

private fun distributionTest() {
    val random = java.util.Random()
    random.nextGaussian()

    val distribution = (0..600).map { random.nextGaussian() * 15 + 65 }
    println("Ave: ${distribution.average()}")
    println("Max: ${distribution.maxOrNull()}")
    println("Min: ${distribution.minOrNull()}")
    println("Players > 100: ${distribution.filter { it > 100 }.size}")
    println("Players > 80: ${distribution.filter { it > 80 }.size}")
    println("Players > 70: ${distribution.filter { it > 70 }.size}")
    println("Players < 60: ${distribution.filter { it < 60 }.size}")
    println("Players < 40: ${distribution.filter { it < 40 }.size}")
    println("Players < 25: ${distribution.filter { it < 25 }.size}")

    val recruits = (0..600).map { RecruitFactory.generateRating(random) }
    println("--------------------")
    println("Ave: ${recruits.average()}")
    println("Max: ${recruits.maxOrNull()}")
    println("Min: ${recruits.minOrNull()}")
    println("Players > 99: ${recruits.filter { it > 99 }.size}")
    println("Players > 80: ${recruits.filter { it > 80 }.size}")
    println("Players > 70: ${recruits.filter { it > 70 }.size}")
    println("Players < 60: ${recruits.filter { it > 60 }.size}")
    println("Players < 40: ${recruits.filter { it < 40 }.size}")
}

private fun simulateXSeasons(seasonsToSim: Int) {
    var basketballWorld = createTeams()

    val teamRatings = mutableMapOf<Int, Int>()
    basketballWorld.conferences.forEach { conference ->
        conference.teams.forEach { team ->
            teamRatings[team.teamId] = team.teamRating
        }
    }

    for (year in 2019 until 2019 + seasonsToSim) {
        println("Season: $year")
        basketballWorld = simulateSeason(basketballWorld, year)

        val original = teamRatings.map { it.value }.average()
        val final = basketballWorld.conferences.map { it.teams.map { team -> team.calculateTeamRating() }.average() }.average()
        println("Original: $original - Final $final")
    }

    basketballWorld.conferences.forEach { conference ->
        conference.teams.forEach { team ->
            val diff = team.calculateTeamRating() - teamRatings[team.teamId]!!
            println("${team.name} - Players: ${team.players.size} - Start: ${teamRatings[team.teamId]} - Finish: ${team.calculateTeamRating()} - Diff: $diff")
        }
    }
}

private fun simulateSeason(basketballWorld: BasketballWorld, year: Int): BasketballWorld {
    // Generate schedule
    val conferenceSchedule = mutableListOf<Game>()
    basketballWorld.conferences.forEach { conferenceSchedule.addAll(it.generateSchedule(year)) }
    conferenceSchedule.smartShuffleList(16)

    val nonCon = NonConferenceScheduleGen.generateNonConferenceSchedule(
        basketballWorld.conferences,
        5,
        year
    )
    nonCon.smartShuffleList(16)
    val schedule = nonCon + conferenceSchedule


    // Simulate every game
    schedule.forEach { game ->
        game.simulateFullGame()
        game.homeTeam.doScouting(basketballWorld.recruits)
        game.awayTeam.doScouting(basketballWorld.recruits)
        TeamRecruitInteractor.interactWithRecruits(game.homeTeam, basketballWorld.recruits)
        TeamRecruitInteractor.interactWithRecruits(game.awayTeam, basketballWorld.recruits)
    }

    // Start new season for each team
    basketballWorld.conferences.forEach { conference ->
        conference.teams.forEach { team ->
            startNewSeasonForTeam(team, basketballWorld.recruits)
        }
    }

    // Create new recruits
    val recruits = RecruitFactory.generateRecruits(
        firstNames = listOf("first"),
        lastNames = listOf("last"),
        numberOfRecruits = 100
    )

    // Return update work
    return BasketballWorld(
        basketballWorld.conferences,
        recruits
    )
}

private fun createTeams(): BasketballWorld {
    val teamNames1 = listOf(
        Pair("Team", "A"),
        Pair("Team", "B"),
        Pair("Team", "C"),
        Pair("Team", "D"),
        Pair("Team", "E"),
        Pair("Team", "F"),
        Pair("Team", "G"),
        Pair("Team", "H"),
    )
    val teamNames2 = listOf(
        Pair("Team", "1"),
        Pair("Team", "2"),
        Pair("Team", "3"),
        Pair("Team", "4"),
        Pair("Team", "5"),
        Pair("Team", "6"),
        Pair("Team", "7"),
        Pair("Team", "8"),
    )

    val conferenceA = Conference(
        id = 1,
        name = "Conference A",
        teams = teamNames1.mapIndexed { index, pair ->
            TeamFactory.generateTeam(
                teamId = index,
                schoolName = pair.first,
                mascot = pair.second,
                color = TeamColor.Red,
                teamAbbreviation = pair.second,
                teamRating = 80 + 5 * index,
                conferenceId = 1,
                isUser = false,
                firstNames = listOf("first"),
                lastNames = listOf("last")
            )
        }
    )
    val conferenceB = Conference(
        id = 1,
        name = "Conference B",
        teams = teamNames2.mapIndexed { index, pair ->
            TeamFactory.generateTeam(
                teamId = index + 10,
                schoolName = pair.first,
                mascot = pair.second,
                color = TeamColor.Red,
                teamAbbreviation = pair.second,
                teamRating = 80 + 5 * index,
                conferenceId = 1,
                isUser = false,
                firstNames = listOf("first"),
                lastNames = listOf("last")
            )
        }
    )

    val recruits = RecruitFactory.generateRecruits(
        firstNames = listOf("first"),
        lastNames = listOf("last"),
        numberOfRecruits = 100
    )

    return BasketballWorld(listOf(conferenceA, conferenceB), recruits)
}

private fun startNewSeasonForTeam(team: Team, recruits: List<Recruit>) {
    // Make each player a year older
    team.players.forEach { player ->
        player.year++
    }

    // Remove players who graduated
    team.returningPlayers(team.players.filter { it.year < 4 })

    // Extra improvement for returning players
    for (i in 1..(PRACTICES / 2)) {
        team.roster.forEach { it.runPractice(PracticeType.NO_FOCUS, team.coaches) }
    }

    // Add commits to team
    recruits.filter { it.isCommitted && it.teamCommittedTo == team.teamId }.forEach { commit ->
        team.addNewPlayer(commit.generatePlayer(team.teamId, team.roster.size))
    }

    for (position in 1..5) {
        // Fill empty spots in roster
        while (team.players.filter { it.position == position }.size < 2) {
            team.addNewPlayer(
                PlayerFactory.generatePlayer(
                    "first",
                    "last",
                    position,
                    0,
                    team.teamId,
                    Random.nextInt(WALK_ON_VARIATION) + WALK_ON_MIN,
                    team.players.size
                )
            )
        }

        // Sort roster so that best players start
        val players = team.players.filter { it.position == position }.sortedByDescending { it.getOverallRating() }
        for (index in players.indices) {
            players[index].apply {
                rosterIndex = index * 5 + position - 1
                courtIndex = rosterIndex
                subPosition = rosterIndex
            }
        }
    }

    // Improve all players on team
    for (i in 1..(PRACTICES / 2)) {
        team.roster.forEach { it.runPractice(PracticeType.NO_FOCUS, team.coaches) }
    }

    team.players.sortBy { it.rosterIndex }

    team.startNewSeason()
}

private const val PRACTICES = 50
private const val WALK_ON_VARIATION = 15
private const val WALK_ON_MIN = 20
