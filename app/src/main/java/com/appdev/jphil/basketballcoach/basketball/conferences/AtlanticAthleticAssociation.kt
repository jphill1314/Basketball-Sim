package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location
import kotlin.random.Random

class AtlanticAthleticAssociation(rating: Int) : ConferenceGeneratorDataModel(
    "Atlantic Athletic Association",
    mutableListOf(
        TeamGeneratorDataModel(
            "Miami",
            "Detectives",
            "MIA",
            Location.FL,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Jacksonville",
            "Swordfish",
            "JS",
            Location.FL,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Philadelphia",
            "Founders",
            "PHF",
            Location.PA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Wilmington",
            "Fear",
            "WLM",
            Location.DE,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Savannah",
            "Jellyfish",
            "SJF",
            Location.GA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Norfolk",
            "Eagles",
            "NE",
            Location.VA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Dover",
            "Monsters",
            "DM",
            Location.DE,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Newark",
            "Sea Bass",
            "NSB",
            Location.NJ,
            rating = Random.nextInt(20) + rating - 10
        )
    ),
    rating
)
