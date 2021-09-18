package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location
import kotlin.random.Random

class WesternConference(rating: Int) : ConferenceGeneratorDataModel(
    "Western Conference",
    mutableListOf(
        TeamGeneratorDataModel(
            "Portland",
            "Hipsters",
            "PTL",
            Location.OR,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Seattle",
            "Coffeemakers",
            "SCM",
            Location.WA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Salem",
            "Sharks",
            "SLM",
            Location.OR,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Olympia",
            "Olympians",
            "OO",
            Location.WA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Anchorage",
            "Mushers",
            "AM",
            Location.AK,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Tacoma",
            "Crabs",
            "TC",
            Location.WA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Honolulu",
            "Wave Riders",
            "HWR",
            Location.HI,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Yakima",
            "Yaks",
            "YY",
            Location.WA,
            rating = Random.nextInt(20) + rating - 10
        )
    ),
    rating
)
