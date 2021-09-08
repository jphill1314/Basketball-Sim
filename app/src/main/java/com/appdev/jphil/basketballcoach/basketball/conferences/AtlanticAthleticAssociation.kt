package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location

class AtlanticAthleticAssociation(rating: Int) : ConferenceGeneratorDataModel(
    "Atlantic Athletic Association",
    listOf(
        TeamGeneratorDataModel(
            "Miami",
            "Detectives",
            "MIA",
            Location.FL
        ),
        TeamGeneratorDataModel(
            "Jacksonville",
            "Swordfish",
            "JS",
            Location.FL
        ),
        TeamGeneratorDataModel(
            "Philadelphia",
            "Founders",
            "PHF",
            Location.PA
        ),
        TeamGeneratorDataModel(
            "Wilmington",
            "Fear",
            "WLM",
            Location.DE
        ),
        TeamGeneratorDataModel(
            "Savannah",
            "Jellyfish",
            "SJF",
            Location.GA
        ),
        TeamGeneratorDataModel(
            "Norfolk",
            "Eagles",
            "NE",
            Location.VA
        ),
        TeamGeneratorDataModel(
            "Dover",
            "Monsters",
            "DM",
            Location.DE
        ),
        TeamGeneratorDataModel(
            "Newark",
            "Sea Bass",
            "NSB",
            Location.NJ
        )
    ),
    rating
)
