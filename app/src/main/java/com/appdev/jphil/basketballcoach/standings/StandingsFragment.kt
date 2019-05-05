package com.appdev.jphil.basketballcoach.standings

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
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
        return inflater.inflate(R.layout.fragment_standings, container, false)
    }

    override fun addTeams(standingsDataModels: List<StandingsDataModel>) {
        adapter = StandingsAdapter(teamId, standingsDataModels, presenter, resources)
        view?.apply {
            findViewById<RecyclerView>(R.id.standings_recycler_view)?.apply {
                adapter = this@StandingsFragment.adapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            findViewById<View>(R.id.header).visibility = View.VISIBLE
            findViewById<TextView>(R.id.position).text = resources.getString(R.string.pos)
            findViewById<TextView>(R.id.name).text = resources.getString(R.string.name)
            findViewById<TextView>(R.id.conference_record).text = resources.getString(R.string.conf_w_l)
            findViewById<TextView>(R.id.overall_record).text = resources.getString(R.string.overall_w_l)
        }
    }

    override fun addConferenceNames(names: List<String>) {
        ArrayAdapter<String>(
            context!!,
            R.layout.spinner_title,
            names
        ).also {
            it.setDropDownViewResource(R.layout.spinner_list_item)
            view?.findViewById<Spinner>(R.id.spinner)?.adapter = it
        }
    }

    override fun changeTeamAndConference(teamId: Int, conferenceId: Int) {
        teamManager.changeConference(conferenceId, teamId)
        (activity as? NavigationManager)?.navigateToHomePage()
    }
}