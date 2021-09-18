package com.appdev.jphil.basketballcoach.schedule.ui

import android.os.Bundle
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appdev.jphil.basketballcoach.compose.arch.ComposeFragment
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ScheduleFragment : ComposeFragment() {

    @Inject
    lateinit var vmFactory: ScheduleViewModelFactory
    override val presenter: SchedulePresenter by viewModels { vmFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            presenter.events.collect { event ->
                when (event) {
                    is SchedulePresenter.NavigateToGame -> navigateToGame(event.gameModel)
                    is SchedulePresenter.NavigateToTournament -> navigateToTournament(
                        event.isTournamentExisting,
                        event.tournamentId
                    )
                    is SchedulePresenter.StartNewSeasonEvent -> navigateToNewSeason()
                    is SchedulePresenter.NavigateToSelectionShow -> navigateToSelectionShow()
                }
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    override fun SetContent() {
        ScheduleScreen(
            viewStateFlow = presenter.viewState,
            interactor = presenter
        )
    }

    private fun navigateToGame(gameModel: ScheduleUiModel) {
        findNavController().navigate(
            ScheduleFragmentDirections.toGamePreviewFragment(
                gameId = gameModel.id,
                homeTeamName = gameModel.bottomTeamName,
                awayTeamName = gameModel.topTeamName,
                isUserHomeTeam = gameModel.isHomeTeamUser
            )
        )
    }

    private fun navigateToTournament(isTournamentExisting: Boolean, tournamentId: Int) {
        findNavController().navigate(
            ScheduleFragmentDirections.toTournament(isTournamentExisting, tournamentId)
        )
    }

    private fun navigateToNewSeason() {
        findNavController().navigate(
            ScheduleFragmentDirections.toNewSeason()
        )
    }

    private fun navigateToSelectionShow() {
        findNavController().navigate(
            ScheduleFragmentDirections.toSelectionShow()
        )
    }
}
