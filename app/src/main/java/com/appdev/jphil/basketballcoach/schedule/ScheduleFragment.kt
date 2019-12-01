package com.appdev.jphil.basketballcoach.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.appdev.jphil.basketball.datamodels.ScheduleDataModel
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.simdialog.SimDialog
import com.appdev.jphil.basketballcoach.simdialog.SimDialogState
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ScheduleFragment : Fragment(), ScheduleContract.View {

    @Inject
    lateinit var presenter: ScheduleContract.Presenter
    private lateinit var recyclerView: RecyclerView
    private var adapter: ScheduleAdapter? = null

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

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = null
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
        findNavController().navigate(ScheduleFragmentDirections.actionScheduleToGamePreviewFragment(
            gameId,
            homeName,
            awayName,
            userIsHomeTeam
        ))
    }

    override fun goToConferenceTournament() {
        findNavController().navigate(ScheduleFragmentDirections.actionScheduleFragmentToTournamentFragment())
    }

    override fun showProgressBar() {
        dialog?.dismiss()
        dialog = SimDialog().apply {
            onDialogDismissed = { presenter.cancelSim() }
            isCancelable = false
        }
        dialog?.show(fragmentManager!!, "TAG")
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
}
