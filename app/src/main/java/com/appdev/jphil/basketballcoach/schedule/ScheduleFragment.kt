package com.appdev.jphil.basketballcoach.schedule


import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.game.GameFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_schedule.view.*
import javax.inject.Inject

class ScheduleFragment : Fragment(), ScheduleContract.View {

    @Inject
    lateinit var presenter: ScheduleContract.Presenter
    var teamId = 1
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScheduleAdapter

    private lateinit var fab: FloatingActionButton
    private lateinit var progressBar: ProgressBar

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
    }

    override fun onStop() {
        super.onStop()
        presenter.onViewDetached()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)
        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener { presenter.onFABClicked() }

        progressBar = view.findViewById(R.id.progress_bar)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun displaySchedule(games: List<ScheduleDataModel>, isUsersSchedule: Boolean) {
        adapter = ScheduleAdapter(resources, presenter, isUsersSchedule)
        recyclerView.adapter = adapter
        adapter.games = games
        adapter.notifyDataSetChanged()
    }

    override fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.frame_layout, GameFragment.newInstance(gameId, homeName, awayName, userIsHomeTeam))
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun disableFab() {
        fab.isEnabled = false
    }

    override fun enableFab() {
        fab.isEnabled = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("teamId", teamId)
        super.onSaveInstanceState(outState)
    }

    companion object {
        fun newInstance(teamId: Int): ScheduleFragment {
            return ScheduleFragment().apply {
                this.teamId = teamId
            }
        }
    }
}
