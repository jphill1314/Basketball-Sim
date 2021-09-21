package com.appdev.jphil.basketballcoach.database.recruit

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketballcoach.database.BasketballDatabase

object RecruitDatabaseHelper {

    suspend fun saveRecruits(recruits: List<Recruit>, database: BasketballDatabase) {
        val recruitEntities = mutableListOf<RecruitEntity>()
        val interestEntities = mutableListOf<RecruitInterestEntity>()

        recruits.forEach { recruit ->
            recruitEntities.add(RecruitEntity.from(recruit))
            recruit.recruitInterests.forEach { interest ->
                interestEntities.add(RecruitInterestEntity.from(recruit.id, interest))
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
    suspend fun deleteAllRecruits(database: BasketballDatabase) {
        database.recruitDao().deleteAllRecruitInterests()
        database.recruitDao().deleteAllRecruits()
    }
}
