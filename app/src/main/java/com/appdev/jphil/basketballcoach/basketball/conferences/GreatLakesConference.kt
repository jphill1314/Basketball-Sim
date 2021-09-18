package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location
import kotlin.random.Random

class GreatLakesConference(rating: Int) : ConferenceGeneratorDataModel(
    "Great Lakes Conference",
    mutableListOf(
        TeamGeneratorDataModel(
            "Cleveland",
            "Rockers",
            "CLE",
            Location.OH,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Detroit",
            "Motors",
            "DET",
            Location.MI,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Milwaukee",
            "Horses",
            "MIL",
            Location.WI,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Chicago",
            "Politicians",
            "CHI",
            Location.IL,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Green Bay",
            "Cheese",
            "GB",
            Location.WI,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Indianapolis",
            "Racers",
            "IND",
            Location.IN,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Cincinnati",
            "Log Drivers",
            "CIN",
            Location.OH,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Pittsburgh",
            "Forges",
            "PIT",
            Location.PA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Duluth",
            "Bears",
            "DUL",
            Location.MN,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Toledo",
            "Chipmunks",
            "TLD",
            Location.OH,
            rating = Random.nextInt(20) + rating - 10
        )
    ),
    rating
)
