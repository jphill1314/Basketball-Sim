package com.appdev.jphil.basketballcoach.recruiting

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.recruits.RecruitUtil
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.main.TeamManagerViewModel
import com.appdev.jphil.basketballcoach.main.ViewModelFactory
import com.appdev.jphil.basketballcoach.main.getTeamViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RecruitFragment : Fragment(), RecruitContract.View {

    @Inject
    lateinit var presenter: RecruitContract.Presenter
    @Inject
    lateinit var factory: ViewModelFactory
    private var teamManager: TeamManagerViewModel? = null
    private lateinit var adapter: RecruitAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        val viewModel = ViewModelProviders.of(this).get(RecruitViewModel::class.java)
        if (viewModel.presenter == null) {
            viewModel.presenter = presenter
        } else {
            presenter = viewModel.presenter!!
        }
        teamManager = activity?.getTeamViewModel(factory)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.sort_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_high_to_low -> presenter.onSortSelected()
            R.id.filter_positions -> {
                AlertDialog.Builder(context!!)
                    .setTitle(R.string.filter_positions)
                    .setItems(resources.getStringArray(R.array.position_filters)) { _, which ->
                        presenter.onPositionFilterSelected(which)
                    }
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
        return inflater.inflate(R.layout.fragment_recruiting, container, false)
    }

    override fun displayRecruits(recruits: List<Recruit>, team: Team) {
        adapter = RecruitAdapter(recruits, teamManager?.teamId ?: -1, presenter, resources)
        view?.apply {
            findViewById<RecyclerView>(R.id.recycler_view)?.let {
                it.layoutManager = LinearLayoutManager(context)
                it.adapter = adapter
            }

            findViewById<TextView>(R.id.center)?.text = resources.getString(
                R.string.number_and_parens,
                RecruitUtil.getReturningPlayersAtPosition(team, 1),
                RecruitUtil.getCommitsAtPosition(team, 1)
            )
            findViewById<TextView>(R.id.sg)?.text = resources.getString(
                R.string.number_and_parens,
                RecruitUtil.getReturningPlayersAtPosition(team, 2),
                RecruitUtil.getCommitsAtPosition(team, 2)
            )
            findViewById<TextView>(R.id.sf)?.text = resources.getString(
                R.string.number_and_parens,
                RecruitUtil.getReturningPlayersAtPosition(team, 3),
                RecruitUtil.getCommitsAtPosition(team, 3)
            )
            findViewById<TextView>(R.id.pf)?.text = resources.getString(
                R.string.number_and_parens,
                RecruitUtil.getReturningPlayersAtPosition(team, 4),
                RecruitUtil.getCommitsAtPosition(team, 4)
            )
            findViewById<TextView>(R.id.c)?.text = resources.getString(
                R.string.number_and_parens,
                RecruitUtil.getReturningPlayersAtPosition(team, 5),
                RecruitUtil.getCommitsAtPosition(team, 5)
            )
        }
    }

    override fun goToRecruitOverview(recruitId: Int) {
        findNavController().navigate(
            RecruitFragmentDirections.actionRecruitFragmentToRecruitOverviewFragment(recruitId)
        )
    }

    override fun updateRecruits(recruits: List<Recruit>) {
        adapter.updateRecruits(recruits)
    }
}