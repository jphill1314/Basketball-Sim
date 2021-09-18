package com.appdev.jphil.basketballcoach.customizeteam

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appdev.jphil.basketballcoach.newgame.NewGameRepository
import javax.inject.Inject

class CustomizeVMFactory @Inject constructor(
//    private val params: CustomizePresenter.Params,
    private val args: CustomizeFragmentArgs,
    private val newGameRepository: NewGameRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CustomizePresenter::class.java) -> CustomizePresenter(
                CustomizePresenter.Params(args.initialTeam, args.conferences.conferences),
                newGameRepository
            ) as T
            else -> throw IllegalArgumentException("Cannot create view model from: $modelClass")
        }
    }
}
