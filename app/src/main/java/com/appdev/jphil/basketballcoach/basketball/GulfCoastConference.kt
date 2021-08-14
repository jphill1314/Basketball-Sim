package com.appdev.jphil.basketballcoach.basketball

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel

class GulfCoastConference(rating: Int) : ConferenceGeneratorDataModel(
    "Gulf Coast Conference",
    listOf(
        TeamGeneratorDataModel(
            "Tampa",
            "Crocodiles",
            "TC"
        ),
        TeamGeneratorDataModel(
            "Tallahassee",
            "Alligators",
            "TA"
        ),
        TeamGeneratorDataModel(
            "Mobile",
            "Shipbuilders",
            "MSB"
        ),
        TeamGeneratorDataModel(
            "New Orleans",
            "Party",
            "NOP"
        ),
        TeamGeneratorDataModel(
            "Houston",
            "Scientists",
            "HOU"
        ),
        TeamGeneratorDataModel(
            "San Antonio",
            "Capybaras",
            "SACB"
        ),
        TeamGeneratorDataModel(
            "Austin",
            "Camels",
            "AUS"
        ),
        TeamGeneratorDataModel(
            "Orlando",
            "Beavers",
            "ORL"
        ),
        TeamGeneratorDataModel(
            "Montgomery",
            "Riders",
            "MONT"
        ),
        TeamGeneratorDataModel(
            "Baton Rouge",
            "Squirrels",
            "BRS"
        )
    ),
    rating
)
