package com.appdev.jphil.basketballcoach.startscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appdev.jphil.basketballcoach.newseason.NewGameRepository
import javax.inject.Inject

class StartScreenVMFactory @Inject constructor(
    private val startScreenRepository: StartScreenRepository,
    private val newGameRepository: NewGameRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StartScreenPresenter::class.java) -> StartScreenPresenter(
                startScreenRepository,
                newGameRepository
            ) as T
            else -> throw IllegalArgumentException("Cannot create view model from: $modelClass")
        }
    }
}
