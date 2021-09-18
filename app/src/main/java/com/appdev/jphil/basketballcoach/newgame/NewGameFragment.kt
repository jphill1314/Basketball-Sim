package com.appdev.jphil.basketballcoach.newgame

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appdev.jphil.basketballcoach.compose.arch.ComposeFragment
import com.appdev.jphil.basketballcoach.customizeteam.AllConferences
import kotlinx.coroutines.flow.collect

class NewGameFragment : ComposeFragment() {

    override val presenter: NewGamePresenter by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            presenter.events.collect { event ->
                when (event) {
                    is NewGamePresenter.StartCustomize -> launchRosterScreen(event)
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
        NewGameScreen(viewStateFlow = presenter.state, interactor = presenter)
    }

    private fun launchRosterScreen(event: NewGamePresenter.StartCustomize) {
        findNavController().navigate(
            NewGameFragmentDirections.toCustomize(
                initialTeam = event.userTeam,
                conferences = AllConferences(event.conferences)
            )
        )
    }
}
