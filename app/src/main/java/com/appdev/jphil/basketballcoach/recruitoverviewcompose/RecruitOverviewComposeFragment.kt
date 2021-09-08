package com.appdev.jphil.basketballcoach.recruitoverviewcompose

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.appdev.jphil.basketballcoach.compose.arch.ComposeFragment
import javax.inject.Inject

class RecruitOverviewComposeFragment : ComposeFragment() {

    @Inject
    lateinit var vmFactory: RecruitVmFactory
    override val presenter: RecruitOverviewPresenter by viewModels { vmFactory }

    val extras by navArgs<RecruitOverviewComposeFragmentArgs>()

    @Composable
    override fun SetContent() {
        RecruitOverview(stateFlow = presenter.state, interactor = presenter)
    }
}
