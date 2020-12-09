package com.appdev.jphil.basketballcoach.arch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

abstract class BasePresenter(
    dispatcherProvider: DispatcherProvider
) {
    val coroutineScope = CoroutineScope(SupervisorJob() + dispatcherProvider.main)
}
