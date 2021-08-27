package com.appdev.jphil.basketballcoach.tournament.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.appdev.jphil.basketballcoach.compose.arch.ComposeFragment
import javax.inject.Inject

class TournamentFragment : ComposeFragment() {

    @Inject
    lateinit var vmFactory: TournamentViewModelFactory
    override val presenter: TournamentPresenter by viewModels { vmFactory }

    val args: TournamentFragmentArgs by navArgs()

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    override fun SetContent() {
        TournamentScreen(
            viewStateFlow = presenter.state,
            interactor = presenter
        )
    }
}
