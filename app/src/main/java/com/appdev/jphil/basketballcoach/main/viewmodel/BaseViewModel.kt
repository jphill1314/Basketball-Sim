package com.appdev.jphil.basketballcoach.main.viewmodel

import android.arch.lifecycle.ViewModel
import com.appdev.jphil.basketballcoach.MVPContract

class BaseViewModel<V: MVPContract.View, P: MVPContract.Presenter<V>> : ViewModel() {

    var presenter: P? = null
    set(value) = if (field == null) field = value else field = field

    override fun onCleared() {
        super.onCleared()
        presenter?.onDestroyed()
        presenter = null
    }
}