package com.appdev.jphil.basketballcoach.basketball.conferences

import com.appdev.jphil.basketball.factories.ConferenceGeneratorDataModel
import com.appdev.jphil.basketball.factories.TeamGeneratorDataModel
import com.appdev.jphil.basketball.location.Location
import kotlin.random.Random

class CaliforniaConference(rating: Int) : ConferenceGeneratorDataModel(
    "California Conference",
    mutableListOf(
        TeamGeneratorDataModel(
            "Sacramento",
            "Panthers",
            "SAC",
            Location.CA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "San Francisco",
            "Seals",
            "SFS",
            Location.CA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Los Angeles",
            "Celebrities",
            "LAC",
            Location.CA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "San Diego",
            "Captains",
            "SDC",
            Location.CA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Anaheim",
            "Pufferfish",
            "ANP",
            Location.CA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Long Beach",
            "Anglerfish",
            "LBA",
            Location.CA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "San Jose",
            "Nerds",
            "SJN",
            Location.CA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Oakland",
            "Freighters",
            "OAK",
            Location.CA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Redding",
            "Redwoods",
            "RR",
            Location.CA,
            rating = Random.nextInt(20) + rating - 10
        ),
        TeamGeneratorDataModel(
            "Fresno",
            "Sequoias",
            "FS",
            Location.CA,
            rating = Random.nextInt(20) + rating - 10
        )
    ),
    rating
)
