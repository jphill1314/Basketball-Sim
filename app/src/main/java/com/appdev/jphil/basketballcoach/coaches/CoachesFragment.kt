package com.appdev.jphil.basketballcoach.coaches

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.main.NavigationManager
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class CoachesFragment : Fragment(), CoachesContract.View {

    @Inject
    lateinit var presenter: CoachesContract.Presenter
    var teamId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? NavigationManager)?.let {
            teamId = it.getTeamId()
        }
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_coaches, container, false)
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

    override fun displayCoaches(coaches: List<Coach>) {
        view?.findViewById<RecyclerView>(R.id.recycler_view)?.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = CoachesAdapter(coaches, resources)
        }
    }
}