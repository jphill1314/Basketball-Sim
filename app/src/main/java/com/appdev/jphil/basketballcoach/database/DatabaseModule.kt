package com.appdev.jphil.basketballcoach.database

import android.app.Application
import androidx.room.Room
import com.appdev.jphil.basketballcoach.main.injection.scopes.PerApplication
import dagger.Module
import dagger.Provides

@Module
abstract class DatabaseModule {

    companion object {
        @PerApplication
        @Provides
        fun providesDatabase(application: Application): BasketballDatabase {
            return Room.databaseBuilder(application, BasketballDatabase::class.java, "basketball-db").build()
        }

        @PerApplication
        @Provides
        fun providesGameDao(database: BasketballDatabase) = database.gameDao()

        @PerApplication
        @Provides
        fun providesPlayerDao(database: BasketballDatabase) = database.playerDao()

        @PerApplication
        @Provides
        fun providesTeamDao(database: BasketballDatabase) = database.teamDao()

        @PerApplication
        @Provides
        fun providesConferenceDao(database: BasketballDatabase) = database.conferenceDao()

        @PerApplication
        @Provides
        fun providesCoachDao(database: BasketballDatabase) = database.coachDao()

        @PerApplication
        @Provides
        fun providesRecruitDao(database: BasketballDatabase) = database.recruitDao()

        @PerApplication
        @Provides
        fun providesRelationalDao(database: BasketballDatabase) = database.relationalDao()
    }
}