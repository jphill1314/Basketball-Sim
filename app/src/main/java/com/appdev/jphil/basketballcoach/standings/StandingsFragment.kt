package com.appdev.jphil.basketballcoach.standings

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.main.NavigationManager
import com.appdev.jphil.basketballcoach.main.TeamManager
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class StandingsFragment : Fragment(), StandingsContract.View {

    @Inject
    lateinit var presenter: StandingsContract.Presenter
    @Inject
    lateinit var teamManager: TeamManager
    private val teamId: Int by lazy { teamManager.getTeamId() }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StandingsAdapter

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
        presenter.onViewDetached()
        super.onStop()
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
        teamManager.changeConference(conferenceId, teamId)
        (activity as? NavigationManager)?.navigateToHomePage()
    }
}