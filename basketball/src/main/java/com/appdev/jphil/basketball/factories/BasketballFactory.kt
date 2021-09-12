package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.BasketballWorld
import com.appdev.jphil.basketball.conference.Conference
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.teams.TeamColor

object BasketballFactory {

    fun setupWholeBasketballWorld(
        conferenceDataModels: List<ConferenceGeneratorDataModel>,
        firstNames: List<String>,
        lastNames: List<String>,
        numberOfRecruits: Int
    ): BasketballWorld {
        val conferences = mutableListOf<Conference>()
        var teams = 0
        conferenceDataModels.forEachIndexed { index, dataModel ->
            conferences.add(
                createConference(
                    teams,
                    index,
                    dataModel.name,
                    dataModel.teams,
                    firstNames,
                    lastNames
                )
            )
            teams += dataModel.teams.size
        }

        val recruits = RecruitFactory.generateRecruits(
            firstNames,
            lastNames,
            numberOfRecruits,
            conferences.flatMap { it.teams }
        )

        return BasketballWorld(
            conferences,
            recruits
        )
    }

    private fun createConference(
        startId: Int,
        conferenceId: Int,
        conferenceName: String,
        teamDataModels: List<TeamGeneratorDataModel>,
        firstNames: List<String>,
        lastNames: List<String>
    ): Conference {
        val teams = mutableListOf<Team>()
        teamDataModels.forEachIndexed { index, dataModel ->
            teams.add(
                TeamFactory.generateTeam(
                    startId + index,
                    dataModel.schoolName,
                    dataModel.mascot,
                    TeamColor.random(),
                    dataModel.abbreviation,
                    dataModel.rating + 20,
                    conferenceId,
                    dataModel.isUser,
                    dataModel.location,
                    firstNames,
                    lastNames
                )
            )
        }

        return Conference(
            conferenceId,
            conferenceName,
            teams
        )
    }
}
