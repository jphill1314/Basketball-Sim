package com.appdev.jphil.basketballcoach.newseason.ui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.appdev.jphil.basketballcoach.compose.arch.ComposeFragment
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class NewSeasonFragment : ComposeFragment() {

    @Inject
    lateinit var vmFactory: NewSeasonViewModelFactory
    override val presenter: NewSeasonPresenter by viewModels { vmFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenCreated {
            presenter.events.collect {
                when (it) {
                    is NewSeasonPresenter.StatNewSeasonEvent -> {
                        findNavController().navigate(
                            NewSeasonFragmentDirections.newSeasonToRoster()
                        )
                    }
                }
            }
        }
    }

    @Composable
    override fun SetContent() {
        NewSeasonLoadingView(viewStateFlow = presenter.viewState, interactor = presenter)
    }
}
