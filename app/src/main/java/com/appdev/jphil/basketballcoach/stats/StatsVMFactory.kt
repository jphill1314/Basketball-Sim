package com.appdev.jphil.basketballcoach.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class StatsVMFactory @Inject constructor(
    private val params: StatsPresenter.Params,
    private val statsRepository: StatsRepository,
    private val statsTransformer: StatsTransformer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StatsPresenter::class.java) -> StatsPresenter(
                statsTransformer,
                params,
                statsRepository
            ) as T
            else -> throw IllegalArgumentException("Cannot create view model from: $modelClass")
        }
    }
}
