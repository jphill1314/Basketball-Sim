package com.appdev.jphil.basketballcoach.database

import android.app.Application
import androidx.room.Room
import com.appdev.jphil.basketballcoach.main.injection.scopes.PerApplication
import dagger.Module
import dagger.Provides

@Module
abstract class DatabaseModule {

    @Module
    companion object {
        @JvmStatic
        @PerApplication
        @Provides
        fun providesDatabase(application: Application): BasketballDatabase {
            return Room.databaseBuilder(application, BasketballDatabase::class.java, "basketball-db").build()
        }

        @JvmStatic
        @PerApplication
        @Provides
        fun providesGameDao(database: BasketballDatabase) = database.gameDao()
    }
}