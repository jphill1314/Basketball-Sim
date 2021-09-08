package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location

class NortheasternAthleticAssociation(rating: Int) : ConferenceGeneratorDataModel(
    "Northeastern Athletic Association",
    listOf(
        TeamGeneratorDataModel(
            "Boston",
            "Colonists",
            "BOS",
            Location.MA
        ),
        TeamGeneratorDataModel(
            "Providence",
            "Preachers",
            "POV",
            Location.RI
        ),
        TeamGeneratorDataModel(
            "New York",
            "Liberty",
            "NY",
            Location.NY
        ),
        TeamGeneratorDataModel(
            "Albany",
            "Cougars",
            "ALB",
            Location.NY
        ),
        TeamGeneratorDataModel(
            "Burlington",
            "Fighting Kittens",
            "BUR",
            Location.VT
        ),
        TeamGeneratorDataModel(
            "Manchester",
            "Hunters",
            "MAN",
            Location.NH
        ),
        TeamGeneratorDataModel(
            "Long Island",
            "Particles",
            "LIP",
            Location.NY
        ),
        TeamGeneratorDataModel(
            "New Haven",
            "Whales",
            "NHW",
            Location.CT
        ),
        TeamGeneratorDataModel(
            "Augusta",
            "Lobsters",
            "AUG",
            Location.ME
        ),
        TeamGeneratorDataModel(
            "Springfield",
            "Fame",
            "SPF",
            Location.MA
        )
    ),
    rating
)
