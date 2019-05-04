package com.appdev.jphil.basketballcoach.recruiting

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.main.TeamManager
import com.appdev.jphil.basketballcoach.recruitoverview.RecruitOverviewFragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RecruitFragment : Fragment(), RecruitContract.View {

    @Inject
    lateinit var presenter: RecruitContract.Presenter
    @Inject
    lateinit var teamManager: TeamManager
    private lateinit var adapter: RecruitAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        val viewModel = ViewModelProviders.of(this).get(RecruitViewModel::class.java)
        if (viewModel.presenter == null) {
            viewModel.presenter = presenter
        } else {
            presenter = viewModel.presenter!!
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.sort_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.sort_high_to_low -> presenter.onSortSelected()
            R.id.filter_positions -> {
                AlertDialog.Builder(context!!)
                    .setTitle(R.string.filter_positions)
                    .setItems(resources.getStringArray(R.array.position_filters),
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                presenter.onPositionFilterSelected(which)
                            }
                        })
                    .show()
            }
            R.id.filter_interation -> {
                AlertDialog.Builder(context!!)
                    .setTitle("Interations")
                    .setItems(resources.getStringArray(R.array.interactions_filters),
                        object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                presenter.onInteractionFilterSelected(which - 1)
                            }
                        })
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
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
        return inflater.inflate(R.layout.fragment_coaches, container, false) // TODO: make own layout
    }

    override fun displayRecruits(recruits: List<Recruit>, team: Team) {
        adapter = RecruitAdapter(recruits, teamManager.getTeamId(), team, presenter, resources)
        view?.findViewById<RecyclerView>(R.id.recycler_view)?.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }
    }

    override fun goToRecruitOverview(recruitId: Int) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.frame_layout, RecruitOverviewFragment.newInstance(recruitId))
            ?.addToBackStack(null)
            ?.commit()
    }

    override fun updateRecruits(recruits: List<Recruit>) {
        adapter.updateRecruits(recruits)
    }
}