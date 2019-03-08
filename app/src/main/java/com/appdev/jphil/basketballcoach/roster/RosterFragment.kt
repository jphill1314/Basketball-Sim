package com.appdev.jphil.basketballcoach.roster

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.main.ChangeTeamConfContract
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_roster.view.*
import javax.inject.Inject

class RosterFragment : Fragment(), RosterContract.View {

    @Inject
    lateinit var presenter: RosterContract.Presenter
    private lateinit var adapter: RosterAdapter
    private lateinit var recyclerView: RecyclerView
    var teamId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(teamId == -1) {
            savedInstanceState?.let {
                teamId = it.getInt("teamId")
            }
        }
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
        recyclerView = view.recycler_view
        return view
    }

    override fun displayData(players: MutableList<Player>) {
        adapter = RosterAdapter(players, resources)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("teamId", teamId)
        super.onSaveInstanceState(outState)
    }

    override fun updateTeamAndConference(teamId: Int, conferenceId: Int) {
        (activity as? ChangeTeamConfContract.Listener?)?.changeConference(conferenceId, teamId)
    }

    companion object {
        fun newInstance(teamId: Int): RosterFragment {
            return RosterFragment().apply {
                this.teamId = teamId
            }
        }
    }
}
