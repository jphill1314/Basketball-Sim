package com.appdev.jphil.basketballcoach.customizeteam

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.appdev.jphil.basketballcoach.compose.arch.ComposeFragment
import com.appdev.jphil.basketballcoach.main.ViewModelFactory
import com.appdev.jphil.basketballcoach.main.getTeamViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class CustomizeFragment : ComposeFragment() {

    @Inject
    lateinit var teamFactory: ViewModelFactory

    @Inject
    lateinit var vmFactory: CustomizeVMFactory
    override val presenter by viewModels<CustomizePresenter> { vmFactory }

    val args: CustomizeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            presenter.events.collect { event ->
                when (event) {
                    is CustomizePresenter.StartNewGame -> startNewGame(event)
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            presenter.state.collect {
                if (it.showSpinner) {
                    navManager.hideToolbar()
                }
            }
        }
    }

    @Composable
    override fun SetContent() {
        CustomizeScreen(stateFlow = presenter.state, interactor = presenter)
    }

    private fun startNewGame(event: CustomizePresenter.StartNewGame) {
        requireActivity().getTeamViewModel(teamFactory).changeTeamAndConference(
            teamId = event.teamId,
            conferenceId = event.conferenceId
        )
        findNavController().navigate(CustomizeFragmentDirections.toRoster())
    }
}
