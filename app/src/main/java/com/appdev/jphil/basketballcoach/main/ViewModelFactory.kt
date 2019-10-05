package com.appdev.jphil.basketballcoach.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.game.GameViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val database: BasketballDatabase
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(database) as T
            modelClass.isAssignableFrom(TeamManagerViewModel::class.java) -> TeamManagerViewModel(database) as T
            else -> throw IllegalArgumentException("ViewModel not found")
        }
    }
}