package com.appdev.jphil.basketballcoach.coachoverview

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.coaches.CoachType
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.FragmentCoachOverviewBinding
import com.appdev.jphil.basketballcoach.main.NavigationManager
import dagger.android.support.AndroidSupportInjection
import java.lang.NumberFormatException
import javax.inject.Inject

class CoachOverviewFragment : Fragment(), CoachOverviewContract.View {

    @Inject
    lateinit var presenter: CoachOverviewContract.Presenter
    var coachId = -1

    private lateinit var binding: FragmentCoachOverviewBinding

    // TODO: Make the layout not be awful in landscape

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            if (coachId == -1) {
                coachId = it.getInt(COACH_ID, 0)
            }
        }
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as? NavigationManager)?.setToolbarTitle(resources.getString(R.string.coach_overview))
        binding = FragmentCoachOverviewBinding.inflate(inflater)
        return binding.root
    }

    override fun displayCoach(coach: Coach) {
        binding.apply {
            coachName.text = coach.fullName
            coachRating.text = resources.getString(R.string.rating_colon, coach.getRating())

            recyclerView.apply {
                adapter = CoachAttributeAdapter(coach, resources)
                layoutManager = LinearLayoutManager(context)
            }

            // TODO: hide assignment if not the player's team
            if (coach.type == CoachType.HEAD_COACH) {
                scoutingAssignment.visibility = View.GONE
            } else {
                scoutingAssignment.visibility = View.VISIBLE

                minRating.apply {
                    setText(coach.scoutingAssignment.minRating.toString())
                    addTextChangedListener(createTextListener { value -> presenter.setMinRating(value) })
                }
                maxRating.apply {
                    setText(coach.scoutingAssignment.maxRating.toString())
                    addTextChangedListener(createTextListener { value -> presenter.setMaxRating(value) })
                }
                minPotential.apply {
                    setText(coach.scoutingAssignment.minPotential.toString())
                    addTextChangedListener(createTextListener { value -> presenter.setMinPotential(value) })
                }
                maxPotential.apply {
                    setText(coach.scoutingAssignment.maxPotential.toString())
                    addTextChangedListener(createTextListener { value -> presenter.setMaxPotential(value) })
                }

                pg.apply {
                    isChecked = coach.scoutingAssignment.positions.contains(1)
                    setOnCheckedChangeListener { _, _ -> presenter.positionToggled(1) }
                }
                sg.apply {
                    isChecked = coach.scoutingAssignment.positions.contains(2)
                    setOnCheckedChangeListener { _, _ -> presenter.positionToggled(2) }
                }
                sf.apply {
                    isChecked = coach.scoutingAssignment.positions.contains(3)
                    setOnCheckedChangeListener { _, _ -> presenter.positionToggled(3) }
                }
                pf.apply {
                    isChecked = coach.scoutingAssignment.positions.contains(4)
                    setOnCheckedChangeListener { _, _ -> presenter.positionToggled(4) }
                }
                center.apply {
                    isChecked = coach.scoutingAssignment.positions.contains(5)
                    setOnCheckedChangeListener { _, _ -> presenter.positionToggled(5) }
                }
            }
        }
    }

    private fun createTextListener(listener: (value: Int) -> Unit): TextWatcher {
        return object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                try {
                    listener(s?.toString()?.toInt() ?: -1)
                } catch (e: NumberFormatException) { }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(COACH_ID, coachId)
        super.onSaveInstanceState(outState)
    }

    companion object {
        fun newInstance(coachId: Int): CoachOverviewFragment {
            val fragment = CoachOverviewFragment()
            fragment.coachId = coachId
            return fragment
        }

        private const val COACH_ID = "coachid"
    }
}