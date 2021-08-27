package com.appdev.jphil.basketballcoach.compose.arch

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class ComposePresenter<DS : DataState, VS : ViewState> :
    ViewModel(),
    Transformer<DS, VS>,
    LifecycleObserver {

    abstract val initialDataState: DS

    protected val dataState: MutableStateFlow<DS> by lazy {
        MutableStateFlow(initialDataState)
    }
    lateinit var state: StateFlow<VS>

    private val mutableEvents = MutableSharedFlow<Event>()
    val events = mutableEvents.asSharedFlow()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        viewModelScope.launch {
            state = dataState.map { transform(it) }.stateIn(this)
        }
    }

    protected fun updateState(block: DS.() -> DS) {
        dataState.update { dataState.value.block() }
    }

    protected fun sendEvent(event: Event) {
        viewModelScope.launch {
            mutableEvents.emit(event)
        }
    }

    abstract override fun transform(dataState: DS): VS
}
