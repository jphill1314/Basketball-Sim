package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location

class CaliforniaConference(rating: Int) : ConferenceGeneratorDataModel(
    "California Conference",
    listOf(
        TeamGeneratorDataModel(
            "Sacramento",
            "Panthers",
            "SAC",
            Location.CA
        ),
        TeamGeneratorDataModel(
            "San Francisco",
            "Seals",
            "SFS",
            Location.CA
        ),
        TeamGeneratorDataModel(
            "Los Angeles",
            "Celebrities",
            "LAC",
            Location.CA
        ),
        TeamGeneratorDataModel(
            "San Diego",
            "Captains",
            "SDC",
            Location.CA
        ),
        TeamGeneratorDataModel(
            "Anaheim",
            "Pufferfish",
            "ANP",
            Location.CA
        ),
        TeamGeneratorDataModel(
            "Long Beach",
            "Anglerfish",
            "LBA",
            Location.CA
        ),
        TeamGeneratorDataModel(
            "San Jose",
            "Nerds",
            "SJN",
            Location.CA
        ),
        TeamGeneratorDataModel(
            "Oakland",
            "Freighters",
            "OAK",
            Location.CA
        ),
        TeamGeneratorDataModel(
            "Redding",
            "Redwoods",
            "RR",
            Location.CA
        ),
        TeamGeneratorDataModel(
            "Fresno",
            "Sequoias",
            "FS",
            Location.CA
        )
    ),
    rating
)
