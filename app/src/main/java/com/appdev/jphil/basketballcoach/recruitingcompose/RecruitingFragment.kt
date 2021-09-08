package com.appdev.jphil.basketballcoach.recruitingcompose

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.compose.arch.ComposeFragment
import javax.inject.Inject

class RecruitingFragment : ComposeFragment() {

    @Inject
    lateinit var vmFactory: RecruitingVMFactory
    override val presenter: RecruitingPresenter by viewModels { vmFactory }

    @Composable
    override fun SetContent() {
        RecruitingView(viewStateFlow = presenter.state, interactor = presenter)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_sort_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.rating_high -> presenter.sortRatingHigh()
            R.id.rating_low -> presenter.sortRatingLow()
            R.id.interest_high -> presenter.sortInterestHigh()
            R.id.interest_low -> presenter.sortInterestLow()
            R.id.interaction_most -> presenter.sortInteractionsMost()
            R.id.interaction_least -> presenter.sortInteractionsLeast()
        }
        return super.onOptionsItemSelected(item)
    }
}
