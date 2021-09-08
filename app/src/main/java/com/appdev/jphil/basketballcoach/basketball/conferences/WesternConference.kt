package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location

class WesternConference(rating: Int) : ConferenceGeneratorDataModel(
    "Western Conference",
    listOf(
        TeamGeneratorDataModel(
            "Portland",
            "Hipsters",
            "PTL",
            Location.OR
        ),
        TeamGeneratorDataModel(
            "Seattle",
            "Coffeemakers",
            "SCM",
            Location.WA
        ),
        TeamGeneratorDataModel(
            "Salem",
            "Sharks",
            "SLM",
            Location.OR
        ),
        TeamGeneratorDataModel(
            "Olympia",
            "Olympians",
            "OO",
            Location.WA
        ),
        TeamGeneratorDataModel(
            "Anchorage",
            "Mushers",
            "AM",
            Location.AK
        ),
        TeamGeneratorDataModel(
            "Tacoma",
            "Crabs",
            "TC",
            Location.WA
        ),
        TeamGeneratorDataModel(
            "Honolulu",
            "Wave Riders",
            "HWR",
            Location.HI
        ),
        TeamGeneratorDataModel(
            "Yakima",
            "Yaks",
            "YY",
            Location.WA
        )
    ),
    rating
)
