package com.appdev.jphil.basketballcoach.simdialog

import androidx.lifecycle.MutableLiveData

data class SimDialogState(
    val games: MutableLiveData<List<SimDialogDataModel>>,
    val text: MutableLiveData<String>,
    val canCancel: MutableLiveData<Boolean>
) {
    constructor() : this(MutableLiveData(), MutableLiveData(), MutableLiveData())
}
