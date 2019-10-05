package com.appdev.jphil.basketballcoach.main

import androidx.lifecycle.ViewModelProviders

fun MainActivity.getTeamViewModel(factory: ViewModelFactory) =
    ViewModelProviders.of(this, factory).get(TeamManagerViewModel::class.java)
