package com.appdev.jphil.basketballcoach.newgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class NewGameVMFactory @Inject constructor(
    private val newGameRepository: NewGameRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(NewGamePresenter::class.java) -> NewGamePresenter(newGameRepository) as T
            else -> throw IllegalArgumentException("Cannot create view model of type: $modelClass")
        }
    }
}
