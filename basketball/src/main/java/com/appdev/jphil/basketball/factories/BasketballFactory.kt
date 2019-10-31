package com.appdev.jphil.basketball.factories

import com.appdev.jphil.basketball.BasketballWorld
import com.appdev.jphil.basketball.conference.Conference
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.teams.TeamColor
import kotlin.random.Random

object BasketballFactory {

    fun setupWholeBasketballWorld(
        conferenceDataModels: List<ConferenceGeneratorDataModel>,
        firstNames: List<String>,
        lastNames: List<String>
    ): BasketballWorld {
        val conferences = mutableListOf<Conference>()
        var teams = 0
        conferenceDataModels.forEachIndexed { index, dataModel ->
            conferences.add(createConference(
                teams,
                index,
                dataModel.minRating,
                dataModel.name,
                dataModel.teams,
                firstNames,
                lastNames
            ))
            teams += dataModel.teams.size
        }

        val recruits = RecruitFactory.generateRecruits(
            firstNames,
            lastNames,
            conferenceDataModels.size * 10
        )

        return BasketballWorld(
            conferences,
            recruits
        )
    }

    private fun createConference(
        startId: Int,
        conferenceId: Int,
        minRating: Int,
        conferenceName: String,
        teamDataModels: List<TeamGeneratorDataModel>,
        firstNames: List<String>,
        lastNames: List<String>
    ): Conference {
        val teams = mutableListOf<Team>()
        teamDataModels.forEachIndexed { index, dataModel ->
            teams.add(TeamFactory.generateTeam(
                startId + index,
                dataModel.schoolName,
                dataModel.mascot,
                TeamColor.random(),
                dataModel.abbreviation,
                minRating + Random.nextInt(15) + dataModel.ratingVariance,
                conferenceId,
                index == 1 && conferenceId == 0, // TODO: make this not bad
                firstNames,
                lastNames
            ))
        }

        return Conference(
            conferenceId,
            conferenceName,
            teams
        )
    }
}