package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location

class MiddleAmericaConference(rating: Int) : ConferenceGeneratorDataModel(
    "Middle America Conference",
    listOf(
        TeamGeneratorDataModel(
            "Dallas",
            "Engineers",
            "DE",
            Location.TX
        ),
        TeamGeneratorDataModel(
            "Ft. Worth",
            "Planes",
            "FWP",
            Location.TX
        ),
        TeamGeneratorDataModel(
            "Oklahoma City",
            "Lemurs",
            "OKC",
            Location.OK
        ),
        TeamGeneratorDataModel(
            "Kansas City",
            "Wagon Riders",
            "KCWR",
            Location.KS
        ),
        TeamGeneratorDataModel(
            "St. Louis",
            "Explorers",
            "STL",
            Location.MO
        ),
        TeamGeneratorDataModel(
            "Iowa City",
            "Iowians",
            "ICI",
            Location.IA
        ),
        TeamGeneratorDataModel(
            "Omaha",
            "Ballers",
            "OB",
            Location.NE
        ),
        TeamGeneratorDataModel(
            "Minneapolis",
            "Koalas",
            "MNK",
            Location.MN
        ),
        TeamGeneratorDataModel(
            "Wichita",
            "Wombats",
            "WW",
            Location.KS
        ),
        TeamGeneratorDataModel(
            "Des Moines",
            "Harvesters",
            "DMH",
            Location.IA
        )
    ),
    rating
)
