package com.appdev.jphil.basketballcoach.schedulecompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appdev.jphil.basketballcoach.newseason.NewSeasonRepository
import com.appdev.jphil.basketballcoach.schedulecompose.data.ScheduleRepository
import com.appdev.jphil.basketballcoach.simulation.GameSimRepository2
import javax.inject.Inject

class ScheduleViewModelFactory @Inject constructor(
    private val repository: ScheduleRepository,
    private val gameSimRepository2: GameSimRepository2,
    private val newSeasonRepository: NewSeasonRepository,
    private val transformer: ScheduleTransformer,
    private val params: SchedulePresenter.Params
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SchedulePresenter::class.java) -> SchedulePresenter(
                params,
                transformer,
                repository,
                gameSimRepository2,
                newSeasonRepository
            ) as T
            else -> throw IllegalArgumentException("ViewModel not found for type: $modelClass")
        }
    }
}
