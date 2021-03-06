package com.appdev.jphil.basketballcoach.coaches

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketballcoach.R
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class CoachesFragment : Fragment(), CoachesContract.View {

    @Inject
    lateinit var presenter: CoachesContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_coaches, container, false)
    }

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

    private fun openCoachOverview(coachId: Int) {
        findNavController().navigate(
            CoachesFragmentDirections.actionCoachesFragmentToCoachOverviewFragment(coachId)
        )
    }

    override fun displayCoaches(coaches: List<Coach>) {
        view?.findViewById<RecyclerView>(R.id.recycler_view)?.let {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = CoachesAdapter(coaches, resources) { coachId -> openCoachOverview(coachId) }
        }
    }
}