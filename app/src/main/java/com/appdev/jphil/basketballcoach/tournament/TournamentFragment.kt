package com.appdev.jphil.basketballcoach.tournament

import android.content.Context
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
import com.appdev.jphil.basketballcoach.main.NavigationManager
import com.appdev.jphil.basketballcoach.schedule.ScheduleDataModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class TournamentFragment : Fragment(), TournamentContract.View {

    @Inject
    lateinit var presenter: TournamentContract.Presenter

    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: TournamentAdapter
    var confId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? NavigationManager)?.let {
            confId = it.getConferenceId()
        }
        AndroidSupportInjection.inject(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewAttached(this)
    }

    override fun onStop() {
        presenter.onViewDetached()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CONF_ID, confId)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)
        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener { presenter.onFABClicked() }
        fab.show()

        view.findViewById<ProgressBar>(R.id.progress_bar).apply { visibility = View.GONE }

        adapter = TournamentAdapter(resources, mutableListOf(), presenter)
        view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@TournamentFragment.adapter
        }
        return view
    }

    override fun onTournamentLoaded(dataModels: MutableList<ScheduleDataModel>) {
        adapter.updateGames(dataModels)
    }

    override fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.frame_layout, GameFragment.newInstance(gameId, homeName, awayName, userIsHomeTeam))
            ?.addToBackStack(null)
            ?.commit()
    }

    companion object {
        private const val CONF_ID = "confID"

        fun newInstance(): TournamentFragment {
            return TournamentFragment()
        }
    }
}