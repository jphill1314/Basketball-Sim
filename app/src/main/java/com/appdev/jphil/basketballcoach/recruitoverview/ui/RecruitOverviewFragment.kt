package com.appdev.jphil.basketballcoach.recruitoverview.ui

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.appdev.jphil.basketballcoach.compose.arch.ComposeFragment
import javax.inject.Inject

class RecruitOverviewFragment : ComposeFragment() {

    @Inject
    lateinit var vmFactory: RecruitVmFactory
    override val presenter: RecruitOverviewPresenter by viewModels { vmFactory }

    val initialData by navArgs<RecruitOverviewFragmentArgs>()

    @Composable
    override fun SetContent() {
        RecruitOverview(stateFlow = presenter.viewState, interactor = presenter)
    }
}
