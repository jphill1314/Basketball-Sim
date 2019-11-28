package com.appdev.jphil.basketballcoach.schedule

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.appdev.jphil.basketball.datamodels.ScheduleDataModel
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.game.GameFragment
import com.appdev.jphil.basketballcoach.main.NavigationManager
import com.appdev.jphil.basketballcoach.simdialog.SimDialog
import com.appdev.jphil.basketballcoach.simdialog.SimDialogDataModel
import com.appdev.jphil.basketballcoach.simdialog.SimDialogState
import com.appdev.jphil.basketballcoach.tournament.TournamentFragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ScheduleFragment : Fragment(), ScheduleContract.View {

    @Inject
    lateinit var presenter: ScheduleContract.Presenter
    private lateinit var recyclerView: RecyclerView
    private var adapter: ScheduleAdapter? = null

    private lateinit var fab: FloatingActionButton
    private var dialog: SimDialog? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AndroidSupportInjection.inject(this)
        val vm = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
        vm.presenter?.let { presenter = it } ?: run { vm.presenter = presenter }
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewAttached(this)
    }

    override fun onPause() {
        dialog?.dismiss()
        super.onPause()
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

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = null
        (activity as? NavigationManager)?.setToolbarTitle(resources.getString(R.string.schedule))
        return view
    }

    override fun displaySchedule(games: List<ScheduleDataModel>, isUsersSchedule: Boolean) {
        if (adapter == null) {
            adapter = ScheduleAdapter(resources, presenter, isUsersSchedule)
            recyclerView.adapter = adapter
        }
        adapter?.games = games
        adapter?.notifyDataSetChanged()
    }

    override fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.frame_layout, GameFragment.newInstance(gameId, homeName, awayName, userIsHomeTeam))
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun goToConferenceTournament() {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.frame_layout, TournamentFragment())
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun showProgressBar() {
        dialog?.dismiss()
        dialog = SimDialog().apply {
            onDialogDismissed = { presenter.cancelSim() }
            isCancelable = false
        }
        dialog?.show(fragmentManager, "TAG")
    }

    override fun setDialogState(state: SimDialogState) {
        if (dialog == null) {
            showProgressBar()
        }
        dialog?.setState(state)
    }

    override fun hideProgressBar() {
        dialog?.dismiss()
    }

    override fun disableFab() {
        fab.hide()
    }

    override fun enableFab() {
        fab.show()
    }
}
