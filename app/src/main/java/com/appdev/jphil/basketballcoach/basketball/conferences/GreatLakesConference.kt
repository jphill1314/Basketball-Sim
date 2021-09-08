package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location

class GreatLakesConference(rating: Int) : ConferenceGeneratorDataModel(
    "Great Lakes Conference",
    listOf(
        TeamGeneratorDataModel(
            "Cleveland",
            "Rockers",
            "CLE",
            Location.OH
        ),
        TeamGeneratorDataModel(
            "Detroit",
            "Motors",
            "DET",
            Location.MI
        ),
        TeamGeneratorDataModel(
            "Milwaukee",
            "Horses",
            "MIL",
            Location.WI
        ),
        TeamGeneratorDataModel(
            "Chicago",
            "Politicians",
            "CHI",
            Location.IL
        ),
        TeamGeneratorDataModel(
            "Green Bay",
            "Cheese",
            "GB",
            Location.WI
        ),
        TeamGeneratorDataModel(
            "Indianapolis",
            "Racers",
            "IND",
            Location.IN
        ),
        TeamGeneratorDataModel(
            "Cincinnati",
            "Log Drivers",
            "CIN",
            Location.OH
        ),
        TeamGeneratorDataModel(
            "Pittsburgh",
            "Forges",
            "PIT",
            Location.PA
        ),
        TeamGeneratorDataModel(
            "Duluth",
            "Bears",
            "DUL",
            Location.MN
        ),
        TeamGeneratorDataModel(
            "Toledo",
            "Chipmunks",
            "TLD",
            Location.OH
        )
    ),
    rating
)
