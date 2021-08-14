package com.appdev.jphil.basketballcoach.database.relations

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RelationalDao {

    @Transaction
    @Query("select * from GameEntity where id in (:gameId)")
    suspend fun loadGameWithTeams(gameId: Int): GameRelations
}
