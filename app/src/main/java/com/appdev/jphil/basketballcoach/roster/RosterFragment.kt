package com.appdev.jphil.basketballcoach.roster

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.main.NavigationManager
import com.appdev.jphil.basketballcoach.playeroverview.PlayerOverviewFragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RosterFragment : Fragment(), RosterContract.View {

    @Inject
    lateinit var presenter: RosterContract.Presenter
    private val adapter: RosterAdapter by lazy { RosterAdapter(presenter, mutableListOf(), resources) }
    private lateinit var recyclerView: RecyclerView

    override fun onResume() {
        super.onResume()
        AndroidSupportInjection.inject(this)
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
        return view
    }

    override fun displayData(players: MutableList<RosterDataModel>, isUsersTeam: Boolean) {
        adapter.isUsersTeam = isUsersTeam
        adapter.updateRoster(players)
        recyclerView.adapter = adapter
    }

    override fun updateTeamAndConference(teamId: Int, conferenceId: Int) {
        (activity as? NavigationManager?)?.changeConference(conferenceId, teamId)
    }

    override fun gotoPlayerOverview(playerId: Int) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.frame_layout, PlayerOverviewFragment.newInstance(playerId))
            ?.addToBackStack(null)
            ?.commit()
    }
}
