package com.appdev.jphil.basketballcoach.basketball

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel

class MountainAthleticAssociation(rating: Int) : ConferenceGeneratorDataModel(
    "Mountain Athletic Association",
    listOf(
        TeamGeneratorDataModel(
            "Denver",
            "Mountaineers",
            "DVM"
        ),
        TeamGeneratorDataModel(
            "Rapid City",
            "Toads",
            "RCT"
        ),
        TeamGeneratorDataModel(
            "Bismark",
            "Bees",
            "BB"
        ),
        TeamGeneratorDataModel(
            "Missoula",
            "Roosters",
            "MR"
        ),
        TeamGeneratorDataModel(
            "Idaho Falls",
            "Farmers",
            "IFF"
        ),
        TeamGeneratorDataModel(
            "Spokane",
            "Sloths",
            "SPK"
        ),
        TeamGeneratorDataModel(
            "Boulder",
            "Basilisks",
            "BLD"
        ),
        TeamGeneratorDataModel(
            "Jackson",
            "Geysers",
            "JG"
        )
    ),
    rating
)