package com.appdev.jphil.basketballcoach.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.database.player.PlayerEntity
import com.appdev.jphil.basketballcoach.database.team.TeamEntity

data class TeamRelations(
    @Embedded val teamEntity: TeamEntity,

    @Relation(
        entity = PlayerEntity::class,
        parentColumn = "teamId",
        entityColumn = "teamId"
    )
    val playerEntities: List<PlayerRelations>,

    @Relation(
        entity = CoachEntity::class,
        parentColumn = "teamId",
        entityColumn = "teamId"
    )
    val coachEntities: List<CoachRelations>
) {

    fun create(allRecruits: List<Recruit>): Team {
        return teamEntity.createTeam(
            players = playerEntities.map { relation ->
                relation.playerEntity.createPlayer().apply {
                    relation.progressions.sortedBy { it.progressionNumber }.forEach {
                        progression.add(it.createProgression(this))
                    }
                }
            }.toMutableList(),
            coaches = coachEntities.map { relation ->
                relation.coachRelations.createCoachWithRecruits(allRecruits)
            }.toMutableList(),
            allRecruits
        )
    }
}
