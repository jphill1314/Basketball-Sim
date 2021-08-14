package com.appdev.jphil.basketballcoach.basketball

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel

class DesertConference(rating: Int) : ConferenceGeneratorDataModel(
    "Desert Conference",
    listOf(
        TeamGeneratorDataModel(
            "Las Vegas",
            "Gamblers",
            "LVG"
        ),
        TeamGeneratorDataModel(
            "Reno",
            "Skiers",
            "RENO"
        ),
        TeamGeneratorDataModel(
            "Phoenix",
            "Drought",
            "PHX"
        ),
        TeamGeneratorDataModel(
            "Tucson",
            "Oxen",
            "TUC"
        ),
        TeamGeneratorDataModel(
            "Albuquerque",
            "Ballons",
            "ALQ"
        ),
        TeamGeneratorDataModel(
            "El Paso",
            "Cowboys",
            "EPC"
        ),
        TeamGeneratorDataModel(
            "Salt Lake City",
            "Saints",
            "SLC"
        ),
        TeamGeneratorDataModel(
            "Amarillo",
            "Armadillos",
            "AA"
        )
    ),
    rating
)
