package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location

class CanadianAthleticConference(rating: Int) : ConferenceGeneratorDataModel(
    "Canadian Athletic Conference",
    listOf(
        TeamGeneratorDataModel(
            "Calgary",
            "Polar Bears",
            "CPB",
            Location.AB
        ),
        TeamGeneratorDataModel(
            "Edmonton",
            "Caribou",
            "EDC",
            Location.AB
        ),
        TeamGeneratorDataModel(
            "Winnipeg",
            "Wolves",
            "WW",
            Location.MB
        ),
        TeamGeneratorDataModel(
            "Toronto",
            "Lions",
            "TL",
            Location.ON
        ),
        TeamGeneratorDataModel(
            "Ottawa",
            "Unicorns",
            "OU",
            Location.ON
        ),
        TeamGeneratorDataModel(
            "Montreal",
            "Narwhals",
            "MN",
            Location.QC
        ),
        TeamGeneratorDataModel(
            "Quebec City",
            "Revolution",
            "QCR",
            Location.QC
        ),
        TeamGeneratorDataModel(
            "Moncton",
            "Chameleons",
            "MC",
            Location.NB
        ),
        TeamGeneratorDataModel(
            "Saskatchewan",
            "Black Bears",
            "SBB",
            Location.SK
        ),
        TeamGeneratorDataModel(
            "Vancouver",
            "Tigers",
            "VAN",
            Location.BC
        )
    ),
    rating
)
