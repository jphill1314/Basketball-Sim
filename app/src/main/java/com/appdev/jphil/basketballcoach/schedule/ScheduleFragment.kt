package com.appdev.jphil.basketballcoach.schedule


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketball.Game

import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.game.GameFragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.fragment_schedule.view.*
import javax.inject.Inject

class ScheduleFragment : Fragment(), ScheduleContract.View {

    @Inject
    lateinit var presenter: ScheduleContract.Presenter
    var teamId = -1
    private val adapter: ScheduleAdapter by lazy { ScheduleAdapter(resources, presenter) }

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
        view.fab.setOnClickListener { presenter.onFABClicked() }
        view.recycler_view.adapter = adapter
        view.recycler_view.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun displaySchedule(games: List<Game>) {
        adapter.games = games
        adapter.notifyDataSetChanged()
    }

    override fun startGameFragment(gameId: Int, homeName: String, awayName: String) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.frame_layout, GameFragment.newInstance(gameId, homeName, awayName))
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun showProgressBar() {
        view?.progress_bar?.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        view?.progress_bar?.visibility = View.GONE
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
