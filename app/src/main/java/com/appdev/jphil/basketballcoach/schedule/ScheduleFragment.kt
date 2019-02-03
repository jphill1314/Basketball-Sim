package com.appdev.jphil.basketballcoach.schedule


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketball.Game

import com.appdev.jphil.basketballcoach.R
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.fragment_schedule.view.*
import javax.inject.Inject

class ScheduleFragment : Fragment(), ScheduleContract.View {

    @Inject
    lateinit var presenter: ScheduleContract.Presenter
    private val adapter: ScheduleAdapter by lazy { ScheduleAdapter(resources) }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewAttached(this)
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
}
