package com.appdev.jphil.basketballcoach.recruiting.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appdev.jphil.basketballcoach.recruiting.data.RecruitingRepository
import javax.inject.Inject

class RecruitingVMFactory @Inject constructor(
    private val params: RecruitingPresenter.Params,
    private val transformer: RecruitingTransformer,
    private val recruitingRepository: RecruitingRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RecruitingPresenter::class.java) -> RecruitingPresenter(
                transformer,
                params,
                recruitingRepository
            ) as T
            else -> throw IllegalArgumentException("Cannot create view model of type $modelClass")
        }
    }
}
