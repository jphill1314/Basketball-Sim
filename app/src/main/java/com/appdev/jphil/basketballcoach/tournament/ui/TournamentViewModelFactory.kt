package com.appdev.jphil.basketballcoach.tournament.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appdev.jphil.basketballcoach.simulation.TournamentSimRepository
import com.appdev.jphil.basketballcoach.tournament.data.TournamentRepository
import javax.inject.Inject

class TournamentViewModelFactory @Inject constructor(
    private val params: TournamentPresenter.Params,
    private val tournamentTransformer: TournamentTransformer,
    private val tournamentRepository: TournamentRepository,
    private val tournamentSimRepository: TournamentSimRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TournamentPresenter::class.java) -> TournamentPresenter(
                params,
                tournamentTransformer,
                tournamentRepository,
                tournamentSimRepository
            ) as T
            else -> throw IllegalArgumentException("Cannot create view model of type $modelClass")
        }
    }
}
