package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location

class TobaccoConference(rating: Int) : ConferenceGeneratorDataModel(
    "Tobacco Conference",
    listOf(
        TeamGeneratorDataModel(
            "DC",
            "Lobbyists",
            "DCL",
            Location.DC
        ),
        TeamGeneratorDataModel(
            "Richmond",
            "Bulls",
            "RCH",
            Location.VA
        ),
        TeamGeneratorDataModel(
            "Charlotte",
            "Bankers",
            "CTL",
            Location.NC
        ),
        TeamGeneratorDataModel(
            "Columbia",
            "Cows",
            "CC",
            Location.SC
        ),
        TeamGeneratorDataModel(
            "Atlanta",
            "News",
            "ATL",
            Location.GA
        ),
        TeamGeneratorDataModel(
            "Baltimore",
            "Fishers",
            "BTL",
            Location.MD
        ),
        TeamGeneratorDataModel(
            "Nashville",
            "Musicians",
            "NSH",
            Location.TN
        ),
        TeamGeneratorDataModel(
            "Raleigh",
            "Hogs",
            "RH",
            Location.NC
        ),
        TeamGeneratorDataModel(
            "Charleston",
            "Plane Builders",
            "CPB",
            Location.SC
        ),
        TeamGeneratorDataModel(
            "Birmingham",
            "Letters",
            "BL",
            Location.AL
        )
    ),
    rating
)
