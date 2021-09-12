package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location
import kotlin.random.Random

class NortheasternAthleticAssociation(rating: Int) : ConferenceGeneratorDataModel(
    "Northeastern Athletic Association",
    mutableListOf(
        TeamGeneratorDataModel(
            "Boston",
            "Colonists",
            "BOS",
            Location.MA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Providence",
            "Preachers",
            "POV",
            Location.RI,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "New York",
            "Liberty",
            "NY",
            Location.NY,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Albany",
            "Cougars",
            "ALB",
            Location.NY,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Burlington",
            "Fighting Kittens",
            "BUR",
            Location.VT,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Manchester",
            "Hunters",
            "MAN",
            Location.NH,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Long Island",
            "Particles",
            "LIP",
            Location.NY,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "New Haven",
            "Whales",
            "NHW",
            Location.CT,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Augusta",
            "Lobsters",
            "AUG",
            Location.ME,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Springfield",
            "Fame",
            "SPF",
            Location.MA,
            rating = Random.nextInt(20) + rating - 10
        )
    ),
    rating
)
