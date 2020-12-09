package com.appdev.jphil.basketballcoach.coaches

import com.appdev.jphil.basketballcoach.arch.BasePresenter
import com.appdev.jphil.basketballcoach.arch.DispatcherProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class CoachesPresenter @Inject constructor(
    private val repository: CoachesContract.Repository,
    dispatcherProvider: DispatcherProvider
) : BasePresenter(dispatcherProvider), CoachesContract.Presenter {

    private var view: CoachesContract.View? = null

    init {
        repository.attachPresenter(this)
    }

    override fun fetchData() {
        coroutineScope.launch {
            view?.displayCoaches(repository.loadCoaches())
        }
    }

    override fun onViewAttached(view: CoachesContract.View) {
        this.view = view
    }

    override fun onViewDetached() {
        view = null
    }
}
