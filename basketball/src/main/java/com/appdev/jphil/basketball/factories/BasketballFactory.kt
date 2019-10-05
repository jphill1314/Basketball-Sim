package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.BasketballWorld
import com.appdev.jphil.basketball.Conference
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.teams.TeamColor

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
        teams.add(TeamFactory.generateTeam(1, "Wofford", "Terriers", TeamColor.Yellow,"WOF", 70, 1, true, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(2, "UNCG", "Spartans", TeamColor.Blue, "UNCG", 65, 1, false, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(3, "ETSU", "Bucs", TeamColor.DeepOrange, "ETSU", 65, 1, false, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(4, "Furman", "Paladins", TeamColor.Purple, "FUR", 60, 1, false, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(5, "Samford", "Bulldogs", TeamColor.BlueGrey, "SAM", 50, 1, false, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(6, "Chattanooga", "Mocs", TeamColor.BlueGrey, "UTC", 45, 1, false, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(7, "Mercer", "Bears", TeamColor.Orange, "MER", 45, 1, false, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(8, "Citadel", "Bulldogs", TeamColor.LightBlue, "CIT", 40, 1, false, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(9, "Western Carolina", "Catamounts", TeamColor.Purple, "WCU", 40, 1, false, firstNames, lastNames))
        teams.add(TeamFactory.generateTeam(10, "VMI", "Keydets", TeamColor.Red, "VMI", 40, 1, false, firstNames, lastNames))
        return Conference(1, "Test Conference", teams)
    }
}