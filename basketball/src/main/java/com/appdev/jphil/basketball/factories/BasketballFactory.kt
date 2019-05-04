package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.BasketballWorld
import com.appdev.jphil.basketball.Conference
import com.appdev.jphil.basketball.teams.Team

object BasketballFactory {

    fun setupWholeBasketballWorld(firstNames: List<String>, lastNames: List<String>): BasketballWorld {
        val conferences = mutableListOf<Conference>()
        conferences.add(createTestConference(TeamFactory(firstNames, lastNames)))
        val recruits = RecruitFactory.generateRecruits(firstNames, lastNames, 100)

        conferences.forEach { conference ->
            conference.teams.forEach { team ->
                recruits.forEach { recruit -> recruit.generateInitialInterest(team) }
            }
        }

        return BasketballWorld(
            conferences,
            recruits
        )
    }

    private fun createTestConference(teamFactory: TeamFactory): Conference {
        val teams = mutableListOf<Team>()
        teams.add(teamFactory.generateTeam(1, "Wofford Terriers", "WOF", 70, 1, true))
        teams.add(teamFactory.generateTeam(2, "UNCG Spartans", "UNCG", 65, 1, false))
        teams.add(teamFactory.generateTeam(3, "ETSU Bucs", "ETSU", 65, 1, false))
        teams.add(teamFactory.generateTeam(4, "Furman Paladins", "FUR", 60, 1, false))
        teams.add(teamFactory.generateTeam(5, "Samford Bulldogs", "SAM", 50, 1, false))
        teams.add(teamFactory.generateTeam(6, "Chattanooga Mocs", "UTC", 45, 1, false))
        teams.add(teamFactory.generateTeam(7, "Mercer Bears", "MER", 45, 1, false))
        teams.add(teamFactory.generateTeam(8, "Citadel Bulldogs", "CIT", 40, 1, false))
        teams.add(teamFactory.generateTeam(9, "Western Carolina Catamounts", "WCU", 40, 1, false))
        teams.add(teamFactory.generateTeam(10, "VMI Keydets", "VMI", 40, 1, false))
        return Conference(1, "Test Conference", teams)
    }
}