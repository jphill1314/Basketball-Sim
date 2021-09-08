package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location

class DesertConference(rating: Int) : ConferenceGeneratorDataModel(
    "Desert Conference",
    listOf(
        TeamGeneratorDataModel(
            "Las Vegas",
            "Gamblers",
            "LVG",
            Location.NV
        ),
        TeamGeneratorDataModel(
            "Reno",
            "Skiers",
            "RENO",
            Location.NV
        ),
        TeamGeneratorDataModel(
            "Phoenix",
            "Drought",
            "PHX",
            Location.AZ
        ),
        TeamGeneratorDataModel(
            "Tucson",
            "Oxen",
            "TUC",
            Location.AZ
        ),
        TeamGeneratorDataModel(
            "Albuquerque",
            "Ballons",
            "ALQ",
            Location.NM
        ),
        TeamGeneratorDataModel(
            "El Paso",
            "Cowboys",
            "EPC",
            Location.TX
        ),
        TeamGeneratorDataModel(
            "Salt Lake City",
            "Saints",
            "SLC",
            Location.UT
        ),
        TeamGeneratorDataModel(
            "Amarillo",
            "Armadillos",
            "AA",
            Location.TX
        )
    ),
    rating
)
