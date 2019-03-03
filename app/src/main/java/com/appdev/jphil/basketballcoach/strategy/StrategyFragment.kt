package com.appdev.jphil.basketballcoach.strategy


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar

import com.appdev.jphil.basketballcoach.R
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class StrategyFragment : Fragment(), StrategyContract.View, SeekBar.OnSeekBarChangeListener {

    private lateinit var pace: SeekBar
    private lateinit var offenseFavorsThrees: SeekBar
    private lateinit var aggression: SeekBar
    private lateinit var defenseFavorsThrees: SeekBar
    private lateinit var pressFrequency: SeekBar
    private lateinit var pressAggression: SeekBar

    @Inject
    lateinit var presenter: StrategyContract.Presenter
    var teamId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (teamId == -1) {
            savedInstanceState?.let {
                teamId = it.getInt("teamId")
            }
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
        val view = inflater.inflate(R.layout.fragment_strategy, container, false)

        pace = view.findViewById(R.id.seekbar_pace)
        pace.setOnSeekBarChangeListener(this)
        offenseFavorsThrees = view.findViewById(R.id.seekbar_offense_favors_threes)
        offenseFavorsThrees.setOnSeekBarChangeListener(this)
        aggression = view.findViewById(R.id.seekbar_aggression)
        aggression.setOnSeekBarChangeListener(this)
        defenseFavorsThrees = view.findViewById(R.id.seekbar_defense_favors_threes)
        defenseFavorsThrees.setOnSeekBarChangeListener(this)
        pressFrequency = view.findViewById(R.id.seekbar_press_frequency)
        pressFrequency.setOnSeekBarChangeListener(this)
        pressAggression = view.findViewById(R.id.seekbar_press_aggression)
        pressAggression.setOnSeekBarChangeListener(this)

        return view
    }

    override fun updateStrategy(strategyDataModel: StrategyDataModel) {
        pace.progress = strategyDataModel.pace
        offenseFavorsThrees.progress = strategyDataModel.offenseFavorsThrees
        aggression.progress = strategyDataModel.aggression
        defenseFavorsThrees.progress = strategyDataModel.defenseFavorsThrees
        pressFrequency.progress = strategyDataModel.pressFrequency
        pressAggression.progress = strategyDataModel.pressAggression
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            R.id.seekbar_pace -> presenter.onPaceChanged(progress)
            R.id.seekbar_offense_favors_threes -> presenter.onOffenseFavorsThreesChanged(progress)
            R.id.seekbar_aggression -> presenter.onAggressionChanged(progress)
            R.id.seekbar_defense_favors_threes -> presenter.onDefenseFavorsThreesChanged(progress)
            R.id.seekbar_press_frequency -> presenter.onPressFrequencyChanged(progress)
            R.id.seekbar_press_aggression -> presenter.onPressAggressionChanged(progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) { }

    override fun onStopTrackingTouch(seekBar: SeekBar?) { }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("teamId", teamId)

        super.onSaveInstanceState(outState)
    }

    companion object {
        fun newInstance(teamId: Int): StrategyFragment {
            val fragment = StrategyFragment()
            fragment.teamId = teamId
            return fragment
        }
    }
}
