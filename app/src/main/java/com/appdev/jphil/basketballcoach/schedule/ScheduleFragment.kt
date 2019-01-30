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
import javax.inject.Inject

class ScheduleFragment : Fragment(), ScheduleContract.View {

    @Inject
    lateinit var presenter: ScheduleContract.Presenter

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun displaySchedule(games: List<Game>) {
        recycler_view.adapter = ScheduleAdapter(games)
        recycler_view.layoutManager = LinearLayoutManager(context)
    }
}
