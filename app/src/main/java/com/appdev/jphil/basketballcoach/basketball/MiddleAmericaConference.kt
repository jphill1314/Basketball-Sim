package com.appdev.jphil.basketballcoach.basketball

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel

class MiddleAmericaConference(rating: Int) : ConferenceGeneratorDataModel(
    "Middle America Conference",
    listOf(
        TeamGeneratorDataModel(
            "Dallas",
            "Engineers",
            "DE"
        ),
        TeamGeneratorDataModel(
            "Ft. Worth",
            "Planes",
            "FWP"
        ),
        TeamGeneratorDataModel(
            "Oklahoma City",
            "Lemurs",
            "OKC"
        ),
        TeamGeneratorDataModel(
            "Kansas City",
            "Wagon Riders",
            "KCWR"
        ),
        TeamGeneratorDataModel(
            "St. Louis",
            "Explorers",
            "STL"
        ),
        TeamGeneratorDataModel(
            "Iowa City",
            "Iowians",
            "ICI"
        ),
        TeamGeneratorDataModel(
            "Omaha",
            "Ballers",
            "OB"
        ),
        TeamGeneratorDataModel(
            "Minneapolis",
            "Koalas",
            "MNK"
        ),
        TeamGeneratorDataModel(
            "Wichita",
            "Wombats",
            "WW"
        ),
        TeamGeneratorDataModel(
            "Des Moines",
            "Harvesters",
            "DMH"
        )
    ),
    rating
)
