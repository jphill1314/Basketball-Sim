package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel

class WesternConference(rating: Int) : ConferenceGeneratorDataModel(
    "Western Conference",
    listOf(
        TeamGeneratorDataModel(
            "Portland",
            "Hipsters",
            "PTL"
        ),
        TeamGeneratorDataModel(
            "Seattle",
            "Coffeemakers",
            "SCM"
        ),
        TeamGeneratorDataModel(
            "Salem",
            "Sharks",
            "SLM"
        ),
        TeamGeneratorDataModel(
            "Olympia",
            "Olympians",
            "OO"
        ),
        TeamGeneratorDataModel(
            "Anchorage",
            "Mushers",
            "AM"
        ),
        TeamGeneratorDataModel(
            "Tacoma",
            "Crabs",
            "TC"
        ),
        TeamGeneratorDataModel(
            "Honolulu",
            "Wave Riders",
            "HWR"
        ),
        TeamGeneratorDataModel(
            "Yakima",
            "Yaks",
            "YY"
        )
    ),
    rating
)
