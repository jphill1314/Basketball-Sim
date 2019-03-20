package com.appdev.jphil.basketballcoach.strategy

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import com.appdev.jphil.basketballcoach.R

class StrategyAdapter(
    private val dataModels: List<StrategyDataModel>,
    private val out: StrategyContract.Adapter.Out
) : RecyclerView.Adapter<StrategyAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val lower: TextView = view.findViewById(R.id.lower)
        val higher: TextView = view.findViewById(R.id.higher)
        val seekbar: SeekBar = view.findViewById(R.id.seekbar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_strategy, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataModels.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = dataModels[position]
        viewHolder.title.text = data.title
        viewHolder.higher.text = data.higher
        viewHolder.lower.text = data.lower
        viewHolder.seekbar.progress = data.value
        viewHolder.seekbar.max = data.max

        viewHolder.seekbar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    when (data.type) {
                        StrategyType.PACE -> out.onPaceChanged(progress)
                        StrategyType.AGGRESSION -> out.onAggressionChanged(progress)
                        StrategyType.OFFENSE_FAVORS_THREES -> out.onOffenseFavorsThreesChanged(progress)
                        StrategyType.DEFENSE_FAVORS_THREES -> out.onDefenseFavorsThreesChanged(progress)
                        StrategyType.PRESS_FREQUENCY -> out.onPressFrequencyChanged(progress)
                        StrategyType.PRESS_AGGRESSION -> out.onPressAggressionChanged(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )
    }
}