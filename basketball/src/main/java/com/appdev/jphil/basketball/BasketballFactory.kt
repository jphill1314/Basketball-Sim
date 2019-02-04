package com.appdev.jphil.basketball

object BasketballFactory {

    fun setupWholeBasketballWorld(teamFactory: TeamFactory): List<Conference> {
        val conferences = mutableListOf<Conference>()
        conferences.add(createTestConference(teamFactory))
        return conferences
    }

    private fun createTestConference(teamFactory: TeamFactory): Conference {
        val teams = mutableListOf<Team>()
        teams.add(teamFactory.generateTeam(1, "Wofford Terriers", 70, 1))
        teams.add(teamFactory.generateTeam(2, "UNCG Spartans", 65, 1))
        teams.add(teamFactory.generateTeam(3, "ETSU Bucs", 65, 1))
        teams.add(teamFactory.generateTeam(4, "Furman Paladins", 60, 1))
        teams.add(teamFactory.generateTeam(5, "Samford Bulldogs", 50, 1))
        teams.add(teamFactory.generateTeam(6, "Chattanooga Mocs", 45, 1))
        teams.add(teamFactory.generateTeam(7, "Mercer Bears", 45, 1))
        teams.add(teamFactory.generateTeam(8, "Citadel Bulldogs", 40, 1))
        teams.add(teamFactory.generateTeam(9, "Western Carolina Catamounts", 40, 1))
        teams.add(teamFactory.generateTeam(10, "VMI Keydets", 40, 1))
        return Conference(1, "Test Conference", teams)
    }
}