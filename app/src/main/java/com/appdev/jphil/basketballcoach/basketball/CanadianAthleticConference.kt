package com.appdev.jphil.basketballcoach.basketball

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel

class CanadianAthleticConference(rating: Int) : ConferenceGeneratorDataModel(
    "Canadian Athletic Conference",
    listOf(
        TeamGeneratorDataModel(
            "Calgary",
            "Polar Bears",
            "CPB"
        ),
        TeamGeneratorDataModel(
            "Edmonton",
            "Caribou",
            "EDC"
        ),
        TeamGeneratorDataModel(
            "Winnipeg",
            "Wolves",
            "WW"
        ),
        TeamGeneratorDataModel(
            "Toronto",
            "Lions",
            "TL"
        ),
        TeamGeneratorDataModel(
            "Ottawa",
            "Unicorns",
            "OU"
        ),
        TeamGeneratorDataModel(
            "Montreal",
            "Narwhals",
            "MN"
        ),
        TeamGeneratorDataModel(
            "Quebec City",
            "Revolution",
            "QCR"
        ),
        TeamGeneratorDataModel(
            "Moncton",
            "Chameleons",
            "MC"
        ),
        TeamGeneratorDataModel(
            "Saskatchewan",
            "Black Bears",
            "SBB"
        ),
        TeamGeneratorDataModel(
            "Vancouver",
            "Tigers",
            "VAN"
        )
    ),
    rating
)
