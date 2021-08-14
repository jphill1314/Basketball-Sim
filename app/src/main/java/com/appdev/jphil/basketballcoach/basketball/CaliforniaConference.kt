package com.appdev.jphil.basketballcoach.basketball

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel

class CaliforniaConference(rating: Int) : ConferenceGeneratorDataModel(
    "California Conference",
    listOf(
        TeamGeneratorDataModel(
            "Sacramento",
            "Panthers",
            "SAC"
        ),
        TeamGeneratorDataModel(
            "San Francisco",
            "Seals",
            "SFS"
        ),
        TeamGeneratorDataModel(
            "Los Angeles",
            "Celebrities",
            "LAC"
        ),
        TeamGeneratorDataModel(
            "San Diego",
            "Captains",
            "SDC"
        ),
        TeamGeneratorDataModel(
            "Anaheim",
            "Pufferfish",
            "ANP"
        ),
        TeamGeneratorDataModel(
            "Long Beach",
            "Anglerfish",
            "LBA"
        ),
        TeamGeneratorDataModel(
            "San Jose",
            "Nerds",
            "SJN"
        ),
        TeamGeneratorDataModel(
            "Oakland",
            "Freighters",
            "OAK"
        ),
        TeamGeneratorDataModel(
            "Redding",
            "Redwoods",
            "RR"
        ),
        TeamGeneratorDataModel(
            "Fresno",
            "Sequoias",
            "FS"
        )
    ),
    rating
)
