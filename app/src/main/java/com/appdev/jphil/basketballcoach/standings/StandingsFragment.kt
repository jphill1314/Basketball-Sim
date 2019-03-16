package com.appdev.jphil.basketballcoach.standings

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.main.NavigationManager
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class StandingsFragment : Fragment(), StandingsContract.View {

    @Inject
    lateinit var presenter: StandingsContract.Presenter
    var conferenceId = 1
    var teamId = 1

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StandingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            conferenceId = it.getInt(CONF_ID_STRING, 1)
            teamId = it.getInt(TEAM_ID_STRING, 1)
        }
        AndroidSupportInjection.inject(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewAttached(this)
        presenter.fetchData()
    }

    override fun onStop() {
        presenter.onViewDetached()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CONF_ID_STRING, conferenceId)
        outState.putInt(TEAM_ID_STRING, teamId)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_standings, container, false).also {
            recyclerView = it.findViewById(R.id.standings_recycler_view)
        }
    }

    override fun addTeams(standingsDataModels: List<StandingsDataModel>) {
        adapter = StandingsAdapter(teamId, standingsDataModels, presenter, resources)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun changeTeamAndConference(teamId: Int, conferenceId: Int) {
        (activity as? NavigationManager?)?.let {
            it.changeConference(conferenceId, teamId)
            it.navigateToHomePage()
        }
    }

    companion object {
        private const val TEAM_ID_STRING = "teamId"
        private const val CONF_ID_STRING = "confId"

        fun newInstance(teamId: Int, conferenceId: Int): StandingsFragment {
            val fragment = StandingsFragment()
            fragment.conferenceId = conferenceId
            fragment.teamId = teamId
            return fragment
        }
    }
}