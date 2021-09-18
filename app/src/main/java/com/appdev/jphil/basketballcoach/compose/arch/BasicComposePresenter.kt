package com.appdev.jphil.basketballcoach.compose.arch

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BasicComposePresenter<DS: DataState> : ViewModel(), LifecycleObserver {

    abstract val initialDataState: DS

    protected val dataState: MutableStateFlow<DS> by lazy { MutableStateFlow(initialDataState) }
    val state: StateFlow<DS> by lazy { dataState.asStateFlow() }

    private val mutableEvents = MutableSharedFlow<Event>()
    val events = mutableEvents.asSharedFlow()

    protected fun updateState(block: DS.() -> DS) {
        dataState.update { dataState.value.block() }
    }

    protected fun sendEvent(event: Event) {
        viewModelScope.launch {
            mutableEvents.emit(event)
        }
    }
}
