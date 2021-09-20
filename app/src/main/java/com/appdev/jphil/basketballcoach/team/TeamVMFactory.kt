package com.appdev.jphil.basketballcoach.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class TeamVMFactory @Inject constructor(
    private val params: TeamPresenter.Params,
    private val repository: TeamRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TeamPresenter::class.java) -> TeamPresenter(
                params,
                repository
            ) as T
            else -> throw IllegalArgumentException("Cannot create view model from: $modelClass")
        }
    }
}
