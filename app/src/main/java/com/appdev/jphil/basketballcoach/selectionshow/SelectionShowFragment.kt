package com.appdev.jphil.basketballcoach.selectionshow

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appdev.jphil.basketballcoach.basketball.NationalChampionshipHelper
import com.appdev.jphil.basketballcoach.compose.arch.ComposeFragment
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SelectionShowFragment : ComposeFragment() {

    @Inject
    lateinit var vmFactory: SelectionShowViewModelFactory
    override val presenter: SelectionShowPresenter by viewModels { vmFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenCreated {
            presenter.events.collect {
                when (it) {
                    is SelectionShowPresenter.EnterTournamentEvent -> {
                        findNavController().navigate(
                            SelectionShowFragmentDirections.selectionShowToTournament(
                                tournamentId = NationalChampionshipHelper.NATIONAL_CHAMPIONSHIP_ID,
                                doesTournamentExist = true
                            )
                        )
                    }
                }
            }
        }
    }

    @Composable
    override fun SetContent() {
        SelectionShowView(viewStateFlow = presenter.viewState, interactor = presenter)
    }
}
