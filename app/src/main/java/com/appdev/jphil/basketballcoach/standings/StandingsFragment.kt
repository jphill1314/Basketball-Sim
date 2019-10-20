package com.appdev.jphil.basketballcoach.standings

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.FragmentStandingsBinding
import com.appdev.jphil.basketballcoach.main.*
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class StandingsFragment : Fragment(), StandingsContract.View {

    @Inject
    lateinit var presenter: StandingsContract.Presenter
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var binding: FragmentStandingsBinding
    private var teamManager: TeamManagerViewModel? = null

    private lateinit var adapter: StandingsAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        teamManager = (activity as? MainActivity)?.getTeamViewModel(viewModelFactory)
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
        (activity as? NavigationManager)?.setToolbarTitle(resources.getString(R.string.standings))
        binding = FragmentStandingsBinding.inflate(inflater)
        return binding.root
    }

    override fun addTeams(standingsDataModels: List<StandingsDataModel>) {
        adapter = StandingsAdapter(teamManager?.teamId ?: -1, standingsDataModels, presenter, resources)
        binding.run {
            standingsRecyclerView.apply {
                adapter = this@StandingsFragment.adapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            header.apply {
                root.visibility = View.VISIBLE
                position.text = resources.getString(R.string.pos)
                name.text = resources.getString(R.string.name)
                conferenceRecord.text = resources.getString(R.string.conf_w_l)
                overallRecord.text = resources.getString(R.string.overall_w_l)
            }
            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    presenter.onConferenceChanged(position)
                }
            }
        }
    }

    override fun addConferenceNames(names: List<String>) {
        if (binding.spinner.adapter == null) {
            ArrayAdapter<String>(
                context!!,
                R.layout.spinner_title,
                names
            ).also {
                it.setDropDownViewResource(R.layout.spinner_list_item)
                binding.spinner.adapter = it
                binding.spinner.setSelection(teamManager?.conferenceId ?: 0, false)
            }
        }
    }

    override fun changeTeamAndConference(teamId: Int, conferenceId: Int) {
        teamManager?.changeTeamAndConference(teamId, conferenceId)
        (activity as? NavigationManager)?.navigateToHomePage()
    }
}