package com.appdev.jphil.basketballcoach.startscreen

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

class StartScreenFragment : ComposeFragment() {

    @Inject
    lateinit var otherVmFactory: ViewModelFactory

    @Inject
    lateinit var vmFactory: StartScreenVMFactory
    override val presenter: StartScreenPresenter by viewModels { vmFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            presenter.events.collect { event ->
                when (event) {
                    is StartScreenPresenter.StartGameEvent -> launchRosterScreen(
                        userId = event.userId,
                        conferenceId = event.conferenceId
                    )
                }
            }
        }
    }

    @Composable
    override fun SetContent() {
        StartScreen(stateFlow = presenter.state, interactor = presenter)
    }

    private fun launchRosterScreen(userId: Int, conferenceId: Int) {
        requireActivity().getTeamViewModel(otherVmFactory).changeTeamAndConference(
            teamId = userId,
            conferenceId = conferenceId
        )
        findNavController().navigate(StartScreenFragmentDirections.toRoster())
    }
}
