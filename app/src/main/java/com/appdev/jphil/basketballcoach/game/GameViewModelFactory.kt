package com.appdev.jphil.basketballcoach.game

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.appdev.jphil.basketballcoach.database.DatabaseHelper
import javax.inject.Inject

class GameViewModelFactory @Inject constructor(
    private val dbHelper: DatabaseHelper
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            GameViewModel(dbHelper) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}