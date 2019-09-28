package com.appdev.jphil.basketballcoach.roster

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.FragmentRosterBinding
import com.appdev.jphil.basketballcoach.main.NavigationManager
import com.appdev.jphil.basketballcoach.main.TeamManager
import com.appdev.jphil.basketballcoach.playeroverview.PlayerOverviewFragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RosterFragment : Fragment(), RosterContract.View {

    @Inject
    lateinit var presenter: RosterContract.Presenter
    @Inject
    lateinit var teamManager: TeamManager
    private lateinit var adapter: RosterAdapter
    private lateinit var binding: FragmentRosterBinding

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
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
        (activity as? NavigationManager)?.setToolbarTitle(resources.getString(R.string.roster))
        return binding.root
    }

    override fun displayData(players: MutableList<RosterDataModel>, team: Team) {
        adapter.isUsersTeam = team.isUser
        adapter.updateRoster(players)
        (activity as? NavigationManager)?.setTeamNameAndRating(team.name, team.teamRating, team.isUser)
    }

    override fun updateTeamAndConference(teamId: Int, conferenceId: Int) {
        teamManager.changeConference(conferenceId, teamId)
    }

    override fun gotoPlayerOverview(playerId: Int) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.frame_layout, PlayerOverviewFragment.newInstance(playerId))
            ?.addToBackStack(null)
            ?.commit()
    }
}
