package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location

class MountainAthleticAssociation(rating: Int) : ConferenceGeneratorDataModel(
    "Mountain Athletic Association",
    listOf(
        TeamGeneratorDataModel(
            "Denver",
            "Mountaineers",
            "DVM",
            Location.CO
        ),
        TeamGeneratorDataModel(
            "Rapid City",
            "Toads",
            "RCT",
            Location.SD
        ),
        TeamGeneratorDataModel(
            "Bismark",
            "Bees",
            "BB",
            Location.ND
        ),
        TeamGeneratorDataModel(
            "Missoula",
            "Roosters",
            "MR",
            Location.MT
        ),
        TeamGeneratorDataModel(
            "Idaho Falls",
            "Farmers",
            "IFF",
            Location.ID
        ),
        TeamGeneratorDataModel(
            "Spokane",
            "Sloths",
            "SPK",
            Location.WA
        ),
        TeamGeneratorDataModel(
            "Boulder",
            "Basilisks",
            "BLD",
            Location.CO
        ),
        TeamGeneratorDataModel(
            "Jackson",
            "Geysers",
            "JG",
            Location.WY
        )
    ),
    rating
)
