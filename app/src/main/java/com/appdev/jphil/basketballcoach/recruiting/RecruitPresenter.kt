package com.appdev.jphil.basketballcoach.recruiting

import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.recruits.RecruitingEvent
import javax.inject.Inject

class RecruitPresenter @Inject constructor(
    private val repository: RecruitContract.Repository
) : RecruitContract.Presenter {

    private var view : RecruitContract.View? = null
    private val sortedRecruits = mutableListOf<Recruit>()
    private var positionFilter = 0
    private var sortHighToLow = true
    private lateinit var team: Team

    init {
        repository.attachPresenter(this)
    }

    override fun fetchData() {
        repository.loadRecruits()
    }

    override fun onRecruitsLoaded(team: Team) {
        this.team = team
        sortedRecruits.clear()
        sortedRecruits.addAll(team.knownRecruits.sortedBy { -it.rating })
        view?.displayRecruits(getRecruits(), team)
    }

    override fun onSortSelected() {
        sortHighToLow = !sortHighToLow
        view?.updateRecruits(getRecruits())
    }

    override fun onPositionFilterSelected(filterType: Int) {
        positionFilter = filterType
        view?.updateRecruits(getRecruits())
    }

    private fun getRecruits(): List<Recruit> {
        return getSortedList(getPositionFilteredList(sortedRecruits))
    }

    private fun getSortedList(list: List<Recruit>): List<Recruit> {
        return if (sortHighToLow) {
            list.sortedBy { -it.rating }
        } else {
            list.sortedBy { it.rating }
        }
    }

    private fun getPositionFilteredList(list: List<Recruit>): List<Recruit> {
        return if (positionFilter == 0) {
            list
        } else {
            list.filter { it.position == positionFilter }
        }
    }

    override fun onRecruitPressed(recruit: Recruit) {
        view?.goToRecruitOverview(recruit.id)
    }

    override fun onViewAttached(view: RecruitContract.View) {
        this.view = view
    }

    override fun onViewDetached() {
        view = null
        repository.saveRecruits(sortedRecruits)
    }
}