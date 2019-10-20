package com.appdev.jphil.basketballcoach.basketball

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel

class GreatLakesConference(rating: Int) : ConferenceGeneratorDataModel(
    "Great Lakes Conference",
    listOf(
        TeamGeneratorDataModel(
            "Cleveland",
            "Rockers",
            "CLE"
        ),
        TeamGeneratorDataModel(
            "Detroit",
            "Motors",
            "DET"
        ),
        TeamGeneratorDataModel(
            "Milwaukee",
            "Horses",
            "MIL"
        ),
        TeamGeneratorDataModel(
            "Chicago",
            "Politicians",
            "CHI"
        ),
        TeamGeneratorDataModel(
            "Green Bay",
            "Cheese",
            "GB"
        ),
        TeamGeneratorDataModel(
            "Indianapolis",
            "Racers",
            "IND"
        ),
         TeamGeneratorDataModel(
             "Cincinnati",
             "Log Drivers",
             "CIN"
         ),
        TeamGeneratorDataModel(
            "Pittsburgh",
            "Forges",
            "PIT"
        ),
        TeamGeneratorDataModel(
            "Duluth",
            "Bears",
            "DUL"
        ),
        TeamGeneratorDataModel(
            "Toledo",
            "Chipmunks",
            "TLD"
        )
    ),
    rating
)