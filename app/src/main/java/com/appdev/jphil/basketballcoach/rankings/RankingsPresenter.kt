package com.appdev.jphil.basketballcoach.rankings

import com.appdev.jphil.basketballcoach.arch.BasePresenter
import com.appdev.jphil.basketballcoach.arch.DispatcherProvider
import kotlinx.coroutines.launch
import javax.inject.Inject

class RankingsPresenter @Inject constructor(
    private val repository: RankingsContract.Repository,
    dispatcherProvider: DispatcherProvider
): BasePresenter(dispatcherProvider), RankingsContract.Presenter {

    private var view: RankingsContract.View? = null

    override fun onViewAttached(view: RankingsContract.View) {
        this.view = view
        repository.attachPresenter(this)

        coroutineScope.launch {
            view.displayData(repository.fetchData())
        }
    }

    override fun onViewDetached() {
        view = null
    }
}
