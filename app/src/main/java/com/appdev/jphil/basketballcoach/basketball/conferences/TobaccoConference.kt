package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location
import kotlin.random.Random

class TobaccoConference(rating: Int) : ConferenceGeneratorDataModel(
    "Tobacco Conference",
    mutableListOf(
        TeamGeneratorDataModel(
            "DC",
            "Lobbyists",
            "DCL",
            Location.DC,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Richmond",
            "Bulls",
            "RCH",
            Location.VA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Charlotte",
            "Bankers",
            "CTL",
            Location.NC,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Columbia",
            "Cows",
            "CC",
            Location.SC,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Atlanta",
            "News",
            "ATL",
            Location.GA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Baltimore",
            "Fishers",
            "BTL",
            Location.MD,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Nashville",
            "Musicians",
            "NSH",
            Location.TN,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Raleigh",
            "Hogs",
            "RH",
            Location.NC,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Charleston",
            "Plane Builders",
            "CPB",
            Location.SC,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Birmingham",
            "Letters",
            "BL",
            Location.AL,
            rating = Random.nextInt(20) + rating - 10
        )
    ),
    rating
)
