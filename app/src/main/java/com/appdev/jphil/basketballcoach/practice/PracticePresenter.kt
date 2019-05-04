package com.appdev.jphil.basketballcoach.practice

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.players.PracticeType
import javax.inject.Inject

class PracticePresenter @Inject constructor(
    private val repository: PracticeContract.Repository
) : PracticeContract.Presenter {

    private var view: PracticeContract.View? = null
    private var team: Team? = null

    init {
        repository.attachPresenter(this)
    }

    override fun fetchData() {
        repository.loadTeam()
    }

    override fun onTeamLoaded(team: Team) {
        view?.displayPracticeInfo(team)
        this.team = team
    }

    override fun onPracticeTypeChanged(type: Int) {
        team?.practiceType = when (type) {
            PracticeType.OFFENSE.type -> PracticeType.OFFENSE
            PracticeType.DEFENSE.type -> PracticeType.DEFENSE
            PracticeType.CONDITIONING.type -> PracticeType.CONDITIONING
            else -> PracticeType.NO_FOCUS
        }
    }

    override fun onViewAttached(view: PracticeContract.View) {
        this.view = view
    }

    override fun onViewDetached() {
        team?.let { repository.saveTeam(it) }
        view = null
    }
}