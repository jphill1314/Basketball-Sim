package com.appdev.jphil.basketballcoach.roster

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.databinding.FragmentRosterBinding
import com.appdev.jphil.basketballcoach.main.TeamManagerViewModel
import com.appdev.jphil.basketballcoach.main.ViewModelFactory
import com.appdev.jphil.basketballcoach.main.getTeamViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RosterFragment : Fragment(), RosterContract.View {

    @Inject
    lateinit var presenter: RosterContract.Presenter
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var teamManager: TeamManagerViewModel? = null
    private lateinit var adapter: RosterAdapter
    private lateinit var binding: FragmentRosterBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        teamManager = activity?.getTeamViewModel(viewModelFactory)
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewAttached(this)
        presenter.fetchData()
    }

    override fun onStop() {
        super.onStop()
        presenter.onViewDetached()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRosterBinding.inflate(inflater)
        adapter = RosterAdapter(presenter, mutableListOf(), resources)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@RosterFragment.adapter
        }
        return binding.root
    }

    override fun displayData(players: MutableList<RosterDataModel>, team: Team) {
        adapter.isUsersTeam = team.isUser
        adapter.updateRoster(players)
    }

    override fun updateTeamAndConference(teamId: Int, conferenceId: Int) {
        teamManager?.changeTeamAndConference(teamId, conferenceId)
    }

    override fun gotoPlayerOverview(playerId: Int) {
        findNavController().navigate(
            RosterFragmentDirections.actionRosterFragmentToPlayerOverviewFragment(playerId)
        )
    }
}
