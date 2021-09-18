package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location
import kotlin.random.Random

class MiddleAmericaConference(rating: Int) : ConferenceGeneratorDataModel(
    "Middle America Conference",
    mutableListOf(
        TeamGeneratorDataModel(
            "Dallas",
            "Engineers",
            "DE",
            Location.TX,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Ft. Worth",
            "Planes",
            "FWP",
            Location.TX,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Oklahoma City",
            "Lemurs",
            "OKC",
            Location.OK,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Kansas City",
            "Wagon Riders",
            "KCWR",
            Location.KS,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "St. Louis",
            "Explorers",
            "STL",
            Location.MO,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Iowa City",
            "Iowians",
            "ICI",
            Location.IA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Omaha",
            "Ballers",
            "OB",
            Location.NE,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Minneapolis",
            "Koalas",
            "MNK",
            Location.MN,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Wichita",
            "Wombats",
            "WW",
            Location.KS,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Des Moines",
            "Harvesters",
            "DMH",
            Location.IA,
            rating = Random.nextInt(20) + rating - 10
        )
    ),
    rating
)
