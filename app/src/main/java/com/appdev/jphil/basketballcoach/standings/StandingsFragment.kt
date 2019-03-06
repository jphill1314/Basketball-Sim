package com.appdev.jphil.basketballcoach.standings

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketballcoach.R
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class StandingsFragment : Fragment(), StandingsContract.View {

    @Inject
    lateinit var presenter: StandingsContract.Presenter
    var conferenceId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            conferenceId = it.getInt("confId", 1)
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
        outState.putInt("confId", conferenceId)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_standings, container, false)
    }

    override fun addTeams(standingsDataModels: List<StandingsDataModel>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.standings_recycler_view)
        recyclerView?.let {
            it.adapter = StandingsAdapter(standingsDataModels, resources)
            it.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    companion object {
        fun newInstance(conferenceId: Int): StandingsFragment {
            val fragment = StandingsFragment()
            fragment.conferenceId = conferenceId
            return fragment
        }
    }
}