package com.appdev.jphil.basketballcoach

interface MVPContract {

    interface View

    interface Presenter<V: View> {
        fun onViewAttached(view: V)
        fun onViewDetached()
    }

    interface Repository<P: Presenter<*>> {
        fun attachPresenter(presenter: P)
    }
}