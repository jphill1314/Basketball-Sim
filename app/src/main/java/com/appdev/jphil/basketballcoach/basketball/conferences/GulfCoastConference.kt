package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location
import kotlin.random.Random

class GulfCoastConference(rating: Int) : ConferenceGeneratorDataModel(
    "Gulf Coast Conference",
    mutableListOf(
        TeamGeneratorDataModel(
            "Tampa",
            "Crocodiles",
            "TC",
            Location.FL,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Tallahassee",
            "Alligators",
            "TA",
            Location.FL,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Mobile",
            "Shipbuilders",
            "MSB",
            Location.AL,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "New Orleans",
            "Party",
            "NOP",
            Location.LA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Houston",
            "Scientists",
            "HOU",
            Location.TX,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "San Antonio",
            "Capybaras",
            "SACB",
            Location.TX,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Austin",
            "Camels",
            "AUS",
            Location.TX,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Orlando",
            "Beavers",
            "ORL",
            Location.TX,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Montgomery",
            "Riders",
            "MONT",
            Location.AL,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Baton Rouge",
            "Squirrels",
            "BRS",
            Location.LA,
            rating = Random.nextInt(20) + rating - 10
        )
    ),
    rating
)
