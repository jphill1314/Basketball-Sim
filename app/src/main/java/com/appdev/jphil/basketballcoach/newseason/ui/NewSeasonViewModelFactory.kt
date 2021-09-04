package com.appdev.jphil.basketballcoach.newseason.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appdev.jphil.basketballcoach.newseason.NewSeasonRepository
import javax.inject.Inject

class NewSeasonViewModelFactory @Inject constructor(
    private val newSeasonRepository: NewSeasonRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(NewSeasonPresenter::class.java) ->
                NewSeasonPresenter(newSeasonRepository) as T
            else -> throw IllegalArgumentException("Cannot create viewmodel of type $modelClass")
        }
    }
}
