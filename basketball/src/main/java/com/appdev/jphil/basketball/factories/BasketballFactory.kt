package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.BasketballWorld
import com.appdev.jphil.basketball.Conference
import com.appdev.jphil.basketball.teams.Team

object BasketballFactory {

    fun setupWholeBasketballWorld(firstNames: List<String>, lastNames: List<String>): BasketballWorld {
        val conferences = mutableListOf<Conference>()
        conferences.add(createTestConference(firstNames, lastNames))
        val recruits = RecruitFactory.generateRecruits(firstNames, lastNames, 100)

        return BasketballWorld(
            conferences,
            recruits
        )
    }

    private fun createTestConference(firstNames: List<String>, lastNames: List<String>): Conference {
        val teams = mutableListOf<Team>()
        teams.add(TeamFactory.generateTeam(1, "Wofford", "Terriers", "WOF", 70, 1, true, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(2, "UNCG", "Spartans", "UNCG", 65, 1, false, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(3, "ETSU", "Bucs", "ETSU", 65, 1, false, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(4, "Furman", "Paladins", "FUR", 60, 1, false, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(5, "Samford", "Bulldogs", "SAM", 50, 1, false, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(6, "Chattanooga", "Mocs", "UTC", 45, 1, false, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(7, "Mercer", "Bears", "MER", 45, 1, false, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(8, "Citadel", "Bulldogs", "CIT", 40, 1, false, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(9, "Western Carolina", "Catamounts", "WCU", 40, 1, false, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(10, "VMI", "Keydets", "VMI", 40, 1, false, firstNames, lastNames))
        return Conference(1, "Test Conference", teams)
    }
}