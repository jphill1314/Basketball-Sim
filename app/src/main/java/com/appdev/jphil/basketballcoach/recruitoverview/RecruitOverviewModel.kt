package com.appdev.jphil.basketballcoach.recruitoverview

import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team

data class RecruitOverviewModel(
    val team: Team,
    val recruit: Recruit
)
