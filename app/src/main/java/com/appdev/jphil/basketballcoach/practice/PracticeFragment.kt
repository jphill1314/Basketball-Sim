package com.appdev.jphil.basketballcoach.practice

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.R
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class PracticeFragment : Fragment(), PracticeContract.View {

    @Inject
    lateinit var presenter: PracticeContract.Presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_practice, container, false).also {
            val spinner = it.findViewById<Spinner>(R.id.practice_type)
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.practice_types,
                R.layout.spinner_title
            ).also { adapter ->
                adapter.setDropDownViewResource(R.layout.spinner_list_item)
                spinner.adapter = adapter
            }

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) { }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    presenter.onPracticeTypeChanged(position)
                }
            }

            it.findViewById<TextView>(R.id.player_name).apply {
                text = resources.getString(R.string.name)
                setTypeface(null, Typeface.BOLD)
            }
            it.findViewById<TextView>(R.id.practice_result).apply {
                text = resources.getString(R.string.practice_performance)
                setTypeface(null, Typeface.BOLD)
            }
        }
    }

    override fun displayPracticeInfo(team: Team) {
        view?.findViewById<Spinner>(R.id.practice_type)?.setSelection(team.practiceType.type)
        view?.findViewById<RecyclerView>(R.id.recycler_view)?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = PracticeAdapter(team.roster)
        }
    }
}