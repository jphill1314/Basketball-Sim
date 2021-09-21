package com.appdev.jphil.basketballcoach.coachoverview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity
import com.appdev.jphil.basketballcoach.databinding.FragmentCoachOverviewBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class CoachOverviewFragment : Fragment(), CoachOverviewContract.View {

    @Inject
    lateinit var presenter: CoachOverviewContract.Presenter
    val args: CoachOverviewFragmentArgs by navArgs()

    private lateinit var binding: FragmentCoachOverviewBinding

    // TODO: Make the layout not be awful in landscape

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.onViewAttached(this)
        presenter.fetchData()
    }

    override fun onStop() {
        super.onStop()
        presenter.onViewDetached()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCoachOverviewBinding.inflate(inflater)
        return binding.root
    }

    override fun displayCoach(coach: CoachEntity) {
        binding.apply {
            coachName.text = resources.getString(R.string.two_strings, coach.firstName, coach.lastName)
            coachRating.text = resources.getString(R.string.rating_colon, coach.rating)

            recyclerView.apply {
                adapter = CoachAttributeAdapter(coach, resources)
                layoutManager = LinearLayoutManager(context)
            }

            scoutingAssignment.visibility = View.GONE
        }
    }
}
