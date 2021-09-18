package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location
import kotlin.random.Random

class MountainAthleticAssociation(rating: Int) : ConferenceGeneratorDataModel(
    "Mountain Athletic Association",
    mutableListOf(
        TeamGeneratorDataModel(
            "Denver",
            "Mountaineers",
            "DVM",
            Location.CO,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Rapid City",
            "Toads",
            "RCT",
            Location.SD,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Bismark",
            "Bees",
            "BB",
            Location.ND,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Missoula",
            "Roosters",
            "MR",
            Location.MT,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Idaho Falls",
            "Farmers",
            "IFF",
            Location.ID,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Spokane",
            "Sloths",
            "SPK",
            Location.WA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Boulder",
            "Basilisks",
            "BLD",
            Location.CO,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Jackson",
            "Geysers",
            "JG",
            Location.WY,
            rating = Random.nextInt(20) + rating - 10
        )
    ),
    rating
)
