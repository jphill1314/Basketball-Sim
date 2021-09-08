package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location

class GulfCoastConference(rating: Int) : ConferenceGeneratorDataModel(
    "Gulf Coast Conference",
    listOf(
        TeamGeneratorDataModel(
            "Tampa",
            "Crocodiles",
            "TC",
            Location.FL
        ),
        TeamGeneratorDataModel(
            "Tallahassee",
            "Alligators",
            "TA",
            Location.FL
        ),
        TeamGeneratorDataModel(
            "Mobile",
            "Shipbuilders",
            "MSB",
            Location.AL
        ),
        TeamGeneratorDataModel(
            "New Orleans",
            "Party",
            "NOP",
            Location.LA
        ),
        TeamGeneratorDataModel(
            "Houston",
            "Scientists",
            "HOU",
            Location.TX
        ),
        TeamGeneratorDataModel(
            "San Antonio",
            "Capybaras",
            "SACB",
            Location.TX
        ),
        TeamGeneratorDataModel(
            "Austin",
            "Camels",
            "AUS",
            Location.TX
        ),
        TeamGeneratorDataModel(
            "Orlando",
            "Beavers",
            "ORL",
            Location.TX
        ),
        TeamGeneratorDataModel(
            "Montgomery",
            "Riders",
            "MONT",
            Location.AL
        ),
        TeamGeneratorDataModel(
            "Baton Rouge",
            "Squirrels",
            "BRS",
            Location.LA
        )
    ),
    rating
)
