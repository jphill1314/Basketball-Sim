package com.appdev.jphil.basketballcoach.stats

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appdev.jphil.basketballcoach.compose.arch.ComposeFragment
import com.appdev.jphil.basketballcoach.main.ViewModelFactory
import com.appdev.jphil.basketballcoach.main.getTeamViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class StatsFragment : ComposeFragment() {

    @Inject
    lateinit var teamsVmFactory: ViewModelFactory
    @Inject
    lateinit var vmFactory: StatsVMFactory
    override val presenter: StatsPresenter by viewModels { vmFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            presenter.events.collect { event ->
                when (event) {
                    is StatsPresenter.SwitchTeams -> switchTeams(event)
                }
            }
        }
    }

    @Composable
    override fun SetContent() {
        StatsView(stateFlow = presenter.viewState, interactor = presenter)
    }

    private fun switchTeams(event: StatsPresenter.SwitchTeams) {
        requireActivity().getTeamViewModel(teamsVmFactory).changeTeamAndConference(
            teamId = event.teamId,
            conferenceId = event.conferenceId
        )
        findNavController().navigate(StatsFragmentDirections.toNewTeamRoster())
    }
}
