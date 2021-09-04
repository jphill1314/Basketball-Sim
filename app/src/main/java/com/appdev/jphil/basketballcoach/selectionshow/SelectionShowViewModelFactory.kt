package com.appdev.jphil.basketballcoach.selectionshow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appdev.jphil.basketballcoach.basketball.NationalChampionshipHelper
import javax.inject.Inject

class SelectionShowViewModelFactory @Inject constructor(
    private val nationalChampionshipHelper: NationalChampionshipHelper
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SelectionShowPresenter::class.java) ->
                SelectionShowPresenter(nationalChampionshipHelper) as T
            else -> throw IllegalArgumentException("Cannot create viewmodel of type $modelClass")
        }
    }
}
