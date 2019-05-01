package com.appdev.jphil.basketballcoach.practice

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.appdev.jphil.basketball.Team
import com.appdev.jphil.basketballcoach.R
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class PracticeFragment : Fragment(), PracticeContract.View {

    @Inject
    lateinit var presenter: PracticeContract.Presenter

    override fun onAttach(context: Context?) {
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
                android.R.layout.simple_spinner_dropdown_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) { }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    presenter.onPracticeTypeChanged(position)
                }
            }
        }
    }

    override fun displayPracticeInfo(team: Team) {
        view?.findViewById<Spinner>(R.id.practice_type)?.setSelection(team.practiceType.type)
    }
}