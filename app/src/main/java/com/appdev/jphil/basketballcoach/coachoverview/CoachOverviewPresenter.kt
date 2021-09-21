package com.appdev.jphil.basketballcoach.coachoverview

import com.appdev.jphil.basketballcoach.arch.BasePresenter
import com.appdev.jphil.basketballcoach.arch.DispatcherProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class CoachOverviewPresenter @Inject constructor(
    private val repository: CoachOverviewContract.Repository,
    dispatcherProvider: DispatcherProvider
) : BasePresenter(dispatcherProvider), CoachOverviewContract.Presenter {

    init {
        repository.attachPresenter(this)
    }

    private var view: CoachOverviewContract.View? = null

    override fun fetchData() {
        coroutineScope.launch {
            view?.displayCoach(repository.loadCoach())
        }
    }

    override fun onViewAttached(view: CoachOverviewContract.View) {
        this.view = view
    }

    override fun onViewDetached() {
        view = null
    }
}
