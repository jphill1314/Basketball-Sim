package com.appdev.jphil.basketballcoach.main

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders

fun FragmentActivity.getTeamViewModel(factory: ViewModelFactory) =
    ViewModelProviders.of(this, factory).get(TeamManagerViewModel::class.java)
