package com.appdev.jphil.basketballcoach.strategy

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketballcoach.R
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class StrategyFragment : Fragment(), StrategyContract.View {

    @Inject
    lateinit var presenter: StrategyContract.Presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
