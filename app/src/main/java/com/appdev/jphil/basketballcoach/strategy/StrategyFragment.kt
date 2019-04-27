package com.appdev.jphil.basketballcoach.strategy


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.main.NavigationManager
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class StrategyFragment : Fragment(), StrategyContract.View {

    @Inject
    lateinit var presenter: StrategyContract.Presenter
    var teamId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? NavigationManager)?.let {
            teamId = it.getTeamId()
        }

        AndroidSupportInjection.inject(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.onViewAttached(this)
        presenter.fetchStrategy()
    }

    override fun onStop() {
        super.onStop()
        presenter.onViewDetached()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_strategy, container, false)
    }

    override fun updateStrategy(strategyDataModels: List<StrategyDataModel>) {
        view?.findViewById<RecyclerView>(R.id.recycler_view)?.let {
            it.adapter = StrategyAdapter(strategyDataModels, presenter)
            it.layoutManager = LinearLayoutManager(context)
        }
    }
}
