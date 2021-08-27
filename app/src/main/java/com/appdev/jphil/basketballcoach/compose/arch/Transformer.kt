package com.appdev.jphil.basketballcoach.compose.arch

interface Transformer<DS : DataState, VS : ViewState> {
    fun transform(dataState: DS): VS
}
