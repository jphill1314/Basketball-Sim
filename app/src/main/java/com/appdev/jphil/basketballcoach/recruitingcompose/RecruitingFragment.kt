package com.appdev.jphil.basketballcoach.recruitingcompose

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.compose.arch.ComposeFragment
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class RecruitingFragment : ComposeFragment() {

    @Inject
    lateinit var vmFactory: RecruitingVMFactory
    override val presenter: RecruitingPresenter by viewModels { vmFactory }

    @Composable
    override fun SetContent() {
        RecruitingView(viewStateFlow = presenter.state, interactor = presenter)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        lifecycleScope.launchWhenCreated {
            presenter.events.collect {
                when (it) {
                    is RecruitingPresenter.LaunchRecruitOverview -> launchRecruitOverview(it.recruitId)
                }
            }
        }
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

    private fun launchRecruitOverview(id: Int) {
        findNavController().navigate(RecruitingFragmentDirections.toRecruitOverview(id))
    }
}
