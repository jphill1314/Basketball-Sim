package com.appdev.jphil.basketballcoach.newgame

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

class NewGameFragment : ComposeFragment() {

    @Inject
    lateinit var otherVmFactory: ViewModelFactory

    @Inject
    lateinit var vmFactory: NewGameVMFactory
    override val presenter: NewGamePresenter by viewModels { vmFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            presenter.events.collect { event ->
                when (event) {
                    is NewGamePresenter.StartNewGame -> launchRosterScreen(
                        userId = event.teamId,
                        conferenceId = event.conferenceId
                    )
                }
            }
        }
    }

    @Composable
    override fun SetContent() {
        NewGameScreen(viewStateFlow = presenter.state, interactor = presenter)
    }

    private fun launchRosterScreen(userId: Int, conferenceId: Int) {
        requireActivity().getTeamViewModel(otherVmFactory).changeTeamAndConference(
            teamId = userId,
            conferenceId = conferenceId
        )
        findNavController().navigate(NewGameFragmentDirections.toRoster())
    }
}
