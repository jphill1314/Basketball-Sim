package com.appdev.jphil.basketballcoach.recruitoverview.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appdev.jphil.basketballcoach.recruitoverview.data.RecruitRepository
import javax.inject.Inject

class RecruitVmFactory @Inject constructor(
    private val params: RecruitOverviewPresenter.Params,
    private val transformer: RecruitOverviewTransformer,
    private val recruitRepository: RecruitRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RecruitOverviewPresenter::class.java) -> RecruitOverviewPresenter(
                transformer,
                params,
                recruitRepository
            ) as T
            else -> throw IllegalArgumentException("Cannot create view model from $modelClass")
        }
    }
}
