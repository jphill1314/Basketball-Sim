package com.appdev.jphil.basketballcoach.database.recruit

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketballcoach.database.BasketballDatabase

object RecruitDatabaseHelper {

    fun saveRecruits(recruits: List<Recruit>, database: BasketballDatabase) {
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

    fun loadAllRecruits(database: BasketballDatabase): MutableList<Recruit> {
        val recruits = mutableListOf<Recruit>()
        database.recruitDao().getAllRecruits().forEach { recruit ->
            recruits.add(recruit.createRecruit(database.recruitDao().getAllInterestsWithRecruitID(recruit.id)))
        }
        return recruits
    }

    fun deleteAllRecruits(database: BasketballDatabase) {
        database.recruitDao().deleteAllRecruitInterests()
        database.recruitDao().deleteAllRecruits()
    }
}