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
import dagger.android.support.AndroidSupportInjection
import java.lang.NumberFormatException
import javax.inject.Inject

class CoachOverviewFragment : Fragment(), CoachOverviewContract.View {

    @Inject
    lateinit var presenter: CoachOverviewContract.Presenter
    var coachId = -1

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
        return inflater.inflate(R.layout.fragment_coach_overview, container, false)
    }

    override fun displayCoach(coach: Coach) {
        view?.apply {
            findViewById<TextView>(R.id.coach_name).text = coach.fullName
            findViewById<TextView>(R.id.coach_rating).text = resources.getString(R.string.rating_colon, coach.getRating())

            findViewById<RecyclerView>(R.id.recycler_view).apply {
                adapter = CoachAttributeAdapter(coach, resources)
                layoutManager = LinearLayoutManager(context)
            }

            if (coach.type == CoachType.HEAD_COACH) {
                findViewById<View>(R.id.scouting_assignment).visibility = View.GONE
            } else {
                findViewById<View>(R.id.scouting_assignment).visibility = View.VISIBLE

                findViewById<EditText>(R.id.min_rating).apply {
                    setText(coach.scoutingAssignment.minRating.toString())
                    addTextChangedListener(createTextListener { value -> presenter.setMinRating(value) })
                }
                findViewById<EditText>(R.id.max_rating).apply {
                    setText(coach.scoutingAssignment.maxRating.toString())
                    addTextChangedListener(createTextListener { value -> presenter.setMaxRating(value) })
                }
                findViewById<EditText>(R.id.min_potential).apply {
                    setText(coach.scoutingAssignment.minPotential.toString())
                    addTextChangedListener(createTextListener { value -> presenter.setMinPotential(value) })
                }
                findViewById<EditText>(R.id.max_potential).apply {
                    setText(coach.scoutingAssignment.maxPotential.toString())
                    addTextChangedListener(createTextListener { value -> presenter.setMaxPotential(value) })
                }

                findViewById<CheckBox>(R.id.pg).apply {
                    isChecked = coach.scoutingAssignment.positions.contains(1)
                    setOnCheckedChangeListener { _, _ -> presenter.positionToggled(1) }
                }
                findViewById<CheckBox>(R.id.sg).apply {
                    isChecked = coach.scoutingAssignment.positions.contains(2)
                    setOnCheckedChangeListener { _, _ -> presenter.positionToggled(2) }
                }
                findViewById<CheckBox>(R.id.sf).apply {
                    isChecked = coach.scoutingAssignment.positions.contains(3)
                    setOnCheckedChangeListener { _, _ -> presenter.positionToggled(3) }
                }
                findViewById<CheckBox>(R.id.pf).apply {
                    isChecked = coach.scoutingAssignment.positions.contains(4)
                    setOnCheckedChangeListener { _, _ -> presenter.positionToggled(4) }
                }
                findViewById<CheckBox>(R.id.center).apply {
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