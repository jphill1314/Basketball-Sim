package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location
import kotlin.random.Random

class CanadianAthleticConference(rating: Int) : ConferenceGeneratorDataModel(
    "Canadian Athletic Conference",
    mutableListOf(
        TeamGeneratorDataModel(
            "Calgary",
            "Polar Bears",
            "CPB",
            Location.AB,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Edmonton",
            "Caribou",
            "EDC",
            Location.AB,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Winnipeg",
            "Wolves",
            "WW",
            Location.MB,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Toronto",
            "Lions",
            "TL",
            Location.ON,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Ottawa",
            "Unicorns",
            "OU",
            Location.ON,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Montreal",
            "Narwhals",
            "MN",
            Location.QC,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Quebec",
            "Revolution",
            "QCR",
            Location.QC,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Moncton",
            "Chameleons",
            "MC",
            Location.NB,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Saskatchewan",
            "Black Bears",
            "SBB",
            Location.SK,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Vancouver",
            "Tigers",
            "VAN",
            Location.BC,
            rating = Random.nextInt(20) + rating - 10
        )
    ),
    rating
)
