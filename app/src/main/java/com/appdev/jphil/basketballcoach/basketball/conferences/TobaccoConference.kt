package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel

class TobaccoConference(rating: Int) : ConferenceGeneratorDataModel(
    "Tobacco Conference",
    listOf(
        TeamGeneratorDataModel(
            "DC",
            "Lobbyists",
            "DCL"
        ),
        TeamGeneratorDataModel(
            "Richmond",
            "Bulls",
            "RCH"
        ),
        TeamGeneratorDataModel(
            "Charlotte",
            "Bankers",
            "CTL"
        ),
        TeamGeneratorDataModel(
            "Columbia",
            "Cows",
            "CC"
        ),
        TeamGeneratorDataModel(
            "Atlanta",
            "News",
            "ATL"
        ),
        TeamGeneratorDataModel(
            "Baltimore",
            "Fishers",
            "BTL"
        ),
        TeamGeneratorDataModel(
            "Nashville",
            "Musicians",
            "NSH"
        ),
        TeamGeneratorDataModel(
            "Raleigh",
            "Hogs",
            "RH"
        ),
        TeamGeneratorDataModel(
            "Charleston",
            "Plane Builders",
            "CPB"
        ),
        TeamGeneratorDataModel(
            "Birmingham",
            "Letters",
            "BL"
        )
    ),
    rating
)
