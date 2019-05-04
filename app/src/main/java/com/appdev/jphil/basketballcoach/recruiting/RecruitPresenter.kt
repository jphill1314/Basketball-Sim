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
    private var interactionFilter = -1
    private var sortHighToLow = true
    private lateinit var team: Team

    init {
        repository.attachPresenter(this)
    }

    override fun fetchData() {
        repository.loadRecruits()
    }

    override fun onRecruitsLoaded(recruits: MutableList<Recruit>, team: Team) {
        this.team = team
        sortedRecruits.clear()
        sortedRecruits.addAll(recruits.sortedBy { -it.rating })
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

    override fun onInteractionFilterSelected(filterType: Int) {
        interactionFilter = filterType
        view?.updateRecruits(getRecruits())
    }

    private fun getRecruits(): List<Recruit> {
        return getSortedList(getInteractionFilteredList(getPositionFilteredList(sortedRecruits)))
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

    private fun getInteractionFilteredList(list: List<Recruit>): List<Recruit> {
        return if (interactionFilter == -1) {
            list
        } else {
            list.filter { getInteractionFilterBool(it) }
        }
    }

    private fun getInteractionFilterBool(recruit: Recruit): Boolean {
        val type = RecruitingEvent.getEventByType(interactionFilter)
        val interests = recruit.interestInTeams.filter { it.teamId == team.teamId }
        if (interests.isEmpty()) {
            return false
        }
        val interest = interests[0]
        return when (type) {
            RecruitingEvent.SCOUT -> interest.isScouted
            RecruitingEvent.COACH_CONTACT -> interest.isContacted
            RecruitingEvent.OFFER_SCHOLARSHIP -> interest.isOfferedScholarship
            RecruitingEvent.OFFICIAL_VISIT -> interest.isOfficialVisitDone
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