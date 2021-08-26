package com.appdev.jphil.basketballcoach.tournamentcompose.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ComposeTournamentFragment : Fragment() {

    @Inject
    lateinit var vmFactory: TournamentViewModelFactory
    private val presenter: TournamentPresenter by viewModels { vmFactory }

    val args: ComposeTournamentFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TournamentScreen(
                    viewStateFlow = presenter.state,
                    interactor = presenter
                )
            }
        }
    }
}
