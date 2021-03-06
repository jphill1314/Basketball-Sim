package com.appdev.jphil.basketballcoach.database.recruit

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketballcoach.database.BasketballDatabase

object RecruitDatabaseHelper {

    suspend fun saveRecruits(recruits: List<Recruit>, database: BasketballDatabase) {
        val recruitEntities = mutableListOf<RecruitEntity>()
        val interestEntities = mutableListOf<RecruitInterestEntity>()

        recruits.forEach { recruit ->
            recruitEntities.add(RecruitEntity.from(recruit))
            recruit.interestInTeams.forEach { interest ->
                interestEntities.add(RecruitInterestEntity.from(interest))
            }
        }

        database.recruitDao().insertRecruits(recruitEntities)
        database.recruitDao().insertInterests(interestEntities)
    }

    suspend fun loadAllRecruits(database: BasketballDatabase): MutableList<Recruit> {
        val recruits = mutableListOf<Recruit>()
        database.recruitDao().getAllRecruits().forEach { recruit ->
            recruits.add(recruit.createRecruit(database.recruitDao().getAllInterestsWithRecruitID(recruit.id)))
        }
        return recruits
    }

    suspend fun loadRecruitWithId(recruitId: Int, database: BasketballDatabase): Recruit {
        val recruit = database.recruitDao().getRecruitWithId(recruitId)
        val interests = database.recruitDao().getAllInterestsWithRecruitID(recruitId)
        return recruit.createRecruit(interests)
    }

    suspend fun deleteAllRecruits(database: BasketballDatabase) {
        database.recruitDao().deleteAllRecruitInterests()
        database.recruitDao().deleteAllRecruits()
    }
}
