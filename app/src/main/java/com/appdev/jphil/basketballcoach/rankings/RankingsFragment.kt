package com.appdev.jphil.basketballcoach.rankings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.advancedmetrics.TeamStatsDataModel
import com.appdev.jphil.basketballcoach.databinding.FragmentRankingsBinding
import com.appdev.jphil.basketballcoach.main.TeamManagerViewModel
import com.appdev.jphil.basketballcoach.main.ViewModelFactory
import com.appdev.jphil.basketballcoach.main.getTeamViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RankingsFragment : Fragment(), RankingsContract.View {

    @Inject
    lateinit var presenter: RankingsContract.Presenter
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var binding: FragmentRankingsBinding
    private var teamViewModel: TeamManagerViewModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onResume() {
        super.onResume()
        teamViewModel = activity?.getTeamViewModel(factory)
        presenter.onViewAttached(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.onViewDetached()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRankingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun displayData(teams: List<TeamStatsDataModel>) {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = RankingsAdapter(teams, teamViewModel?.teamId ?: 0, resources) { teamId, confId ->
                teamViewModel?.changeTeamAndConference(teamId, confId)
            }
        }
        binding.header.apply {
            name.text = resources.getString(R.string.team)
            efficiency.text = resources.getString(R.string.adj_eff)
            tempo.text = "Tempo"
            offEff.text = resources.getString(R.string.adj_off)
            defEff.text = resources.getString(R.string.adj_def)
            color.visibility = View.INVISIBLE
        }
    }
}
