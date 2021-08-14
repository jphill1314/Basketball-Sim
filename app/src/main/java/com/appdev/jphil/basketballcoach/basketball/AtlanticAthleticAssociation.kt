package com.appdev.jphil.basketballcoach.basketball

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel

class AtlanticAthleticAssociation(rating: Int) : ConferenceGeneratorDataModel(
    "Atlantic Athletic Association",
    listOf(
        TeamGeneratorDataModel(
            "Miami",
            "Detectives",
            "MIA"
        ),
        TeamGeneratorDataModel(
            "Jacksonville",
            "Swordfish",
            "JS"
        ),
        TeamGeneratorDataModel(
            "Philadelphia",
            "Founders",
            "PHF"
        ),
        TeamGeneratorDataModel(
            "Wilmington",
            "Fear",
            "WLM"
        ),
        TeamGeneratorDataModel(
            "Savannah",
            "Jellyfish",
            "SJF"
        ),
        TeamGeneratorDataModel(
            "Norfolk",
            "Eagles",
            "NE"
        ),
        TeamGeneratorDataModel(
            "Dover",
            "Monsters",
            "DM"
        ),
        TeamGeneratorDataModel(
            "Newark",
            "Sea Bass",
            "NSB"
        )
    ),
    rating
)
