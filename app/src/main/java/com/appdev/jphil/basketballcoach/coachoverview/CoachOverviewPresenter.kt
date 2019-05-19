package com.appdev.jphil.basketballcoach.coachoverview

import com.appdev.jphil.basketball.coaches.Coach
import javax.inject.Inject

class CoachOverviewPresenter @Inject constructor(
    private val repository: CoachOverviewContract.Repository
): CoachOverviewContract.Presenter {

    init {
        repository.attachPresenter(this)
    }

    private var view: CoachOverviewContract.View? = null
    private lateinit var coach: Coach

    override fun fetchData() {
        repository.loadCoach()
    }

    override fun onCoachLoaded(coach: Coach) {
        this.coach = coach
        view?.displayCoach(coach)
    }

    override fun positionToggled(position: Int) {
        val assignment = coach.scoutingAssignment
        if (assignment.positions.contains(position)) {
            assignment.positions.remove(position)
        } else {
            assignment.positions.add(position)
        }
    }

    override fun setMinRating(minRating: Int) {
        // TODO: warn user when they give a bad value
        // TODO: keep bad values in case they become valid
        if (minRating in 0..100 && minRating <= coach.scoutingAssignment.maxRating) {
            coach.scoutingAssignment.minRating = minRating
        }
    }

    override fun setMaxRating(maxRating: Int) {
        if (maxRating in 0..100 && maxRating >= coach.scoutingAssignment.minRating) {
            coach.scoutingAssignment.maxRating = maxRating
        }
    }

    override fun setMinPotential(minPotential: Int) {
        if (minPotential in 0..100 && minPotential <= coach.scoutingAssignment.maxPotential) {
            coach.scoutingAssignment.minPotential = minPotential
        }
    }

    override fun setMaxPotential(maxPotential: Int) {
        if (maxPotential in 0..100 && maxPotential >= coach.scoutingAssignment.minPotential) {
            coach.scoutingAssignment.maxPotential = maxPotential
        }
    }

    override fun onViewAttached(view: CoachOverviewContract.View) {
        this.view = view
    }

    override fun onViewDetached() {
        view = null
        repository.saveCoach(coach)
    }
}