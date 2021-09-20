package com.appdev.jphil.basketballcoach.team

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appdev.jphil.basketballcoach.compose.arch.ComposeFragment
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class TeamFragment : ComposeFragment() {

    @Inject
    lateinit var vmFactory: TeamVMFactory
    override val presenter: TeamPresenter by viewModels { vmFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            presenter.events.collect { event ->
                when (event) {
                    is TeamPresenter.LaunchPlayerDetail -> launchPlayerOverview(event.playerId)
                    is TeamPresenter.LaunchCoachDetail -> launchCoachOverview(event.coachId)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        navManager.showToolbar()
    }

    @Composable
    override fun SetContent() {
        TeamView(stateFlow = presenter.state, interactor = presenter)
    }

    private fun launchPlayerOverview(playerId: Int) {
        findNavController().navigate(
            TeamFragmentDirections.toPlayerOverview(playerId)
        )
    }

    private fun launchCoachOverview(coachId: Int) {
        findNavController().navigate(
            TeamFragmentDirections.toCoachOverview(coachId)
        )
    }
}
