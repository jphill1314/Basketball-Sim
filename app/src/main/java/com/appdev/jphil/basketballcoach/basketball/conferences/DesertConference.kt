package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location
import kotlin.random.Random

class DesertConference(rating: Int) : ConferenceGeneratorDataModel(
    "Desert Conference",
    mutableListOf(
        TeamGeneratorDataModel(
            "Las Vegas",
            "Gamblers",
            "LVG",
            Location.NV,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Reno",
            "Skiers",
            "RENO",
            Location.NV,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Phoenix",
            "Drought",
            "PHX",
            Location.AZ,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Tucson",
            "Oxen",
            "TUC",
            Location.AZ,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Albuquerque",
            "Ballons",
            "ALQ",
            Location.NM,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "El Paso",
            "Cowboys",
            "EPC",
            Location.TX,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Salt Lake City",
            "Saints",
            "SLC",
            Location.UT,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Amarillo",
            "Armadillos",
            "AA",
            Location.TX,
            rating = Random.nextInt(20) + rating - 10
        )
    ),
    rating
)
