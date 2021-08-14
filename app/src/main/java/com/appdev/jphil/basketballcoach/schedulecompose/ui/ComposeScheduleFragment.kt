package com.appdev.jphil.basketballcoach.schedulecompose.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ComposeScheduleFragment : Fragment() {

    @Inject
    lateinit var vmFactory: ScheduleViewModelFactory
    private val presenter: SchedulePresenter by viewModels { vmFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)

        lifecycleScope.launchWhenCreated {
            presenter.events.collect { event ->
                when (event) {
                    is SchedulePresenter.NavigateToGame -> navigateToGame(event.gameModel)
                }
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ScheduleScreen(
                    viewStateFlow = presenter.state,
                    interactor = presenter
                )
            }
        }
    }

    private fun navigateToGame(gameModel: ScheduleUiModel) {
        findNavController().navigate(
            ComposeScheduleFragmentDirections.toGamePreviewFragment(
                gameId = gameModel.id,
                homeTeamName = gameModel.bottomTeamName,
                awayTeamName = gameModel.topTeamName,
                isUserHomeTeam = gameModel.isHomeTeamUser
            )
        )
    }
}