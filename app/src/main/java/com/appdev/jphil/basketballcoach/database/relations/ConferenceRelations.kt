package com.appdev.jphil.basketballcoach.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.appdev.jphil.basketball.conference.Conference
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketballcoach.database.conference.ConferenceEntity
import com.appdev.jphil.basketballcoach.database.team.TeamEntity

data class ConferenceRelations(
    @Embedded val conferenceEntity: ConferenceEntity,
    @Relation(
        entity = TeamEntity::class,
        parentColumn = "id",
        entityColumn = "conferenceId"
    )
    val teamEntities: List<TeamRelations>,
) {

    fun createConference(allRecruits: List<Recruit>): Conference {
        val teams = teamEntities.map { entity ->
            entity.teamEntity.createTeam(
                players = entity.playerEntities.map { relation ->
                    relation.playerEntity.createPlayer().apply {
                        relation.progressions.sortedBy { it.progressionNumber }.forEach {
                            progression.add(it.createProgression(this))
                        }
                    }
                }.toMutableList(),
                coaches = entity.coachEntities.map { relation ->
                    relation.coachRelations.createCoach(relation.assignmentEntity)
                }.toMutableList(),
                knownRecruits = entity.teamEntity.knownRecruits.map { id ->
                    allRecruits.first { it.id == id }
                }.toMutableList()
            )
        }

        return Conference(
            conferenceEntity.id,
            conferenceEntity.name,
            teams
        )
    }
}
