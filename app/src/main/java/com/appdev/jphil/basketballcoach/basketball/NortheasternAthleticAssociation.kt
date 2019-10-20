package com.appdev.jphil.basketballcoach.basketball

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel

class NortheasternAthleticAssociation(rating: Int) : ConferenceGeneratorDataModel(
    "Northeastern Athletic Association",
    listOf(
        TeamGeneratorDataModel(
            "Boston",
            "Colonists",
            "BOS"
        ),
        TeamGeneratorDataModel(
            "Providence",
            "Preachers",
            "POV"
        ),
        TeamGeneratorDataModel(
            "New York",
            "Liberty",
            "NY"
        ),
        TeamGeneratorDataModel(
            "Albany",
            "Cougars",
            "ALB"
        ),
        TeamGeneratorDataModel(
            "Burlington",
            "Fighting Kittens",
            "BUR"
        ),
        TeamGeneratorDataModel(
            "Manchester",
            "Hunters",
            "MAN"
        ),
        TeamGeneratorDataModel(
            "Long Island",
            "Particles",
            "LIP"
        ),
        TeamGeneratorDataModel(
            "New Haven",
            "Whales",
            "NHW"
        ),
        TeamGeneratorDataModel(
            "Augusta",
            "Lobsters",
            "AUG"
        ),
        TeamGeneratorDataModel(
            "Springfield",
            "Fame",
            "SPF"
        )
    ),
    rating
)