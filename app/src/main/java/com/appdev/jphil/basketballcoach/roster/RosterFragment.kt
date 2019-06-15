package com.appdev.jphil.basketballcoach.roster

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.main.TeamManager
import com.appdev.jphil.basketballcoach.playeroverview.PlayerOverviewFragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RosterFragment : Fragment(), RosterContract.View {

    @Inject
    lateinit var presenter: RosterContract.Presenter
    @Inject
    lateinit var teamManager: TeamManager
    private val adapter: RosterAdapter by lazy { RosterAdapter(presenter, mutableListOf(), resources) }
    private lateinit var recyclerView: RecyclerView

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
        val view = inflater.inflate(R.layout.fragment_roster, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        return view
    }

    override fun displayData(players: MutableList<RosterDataModel>, isUsersTeam: Boolean) {
        adapter.isUsersTeam = isUsersTeam
        adapter.updateRoster(players)
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
