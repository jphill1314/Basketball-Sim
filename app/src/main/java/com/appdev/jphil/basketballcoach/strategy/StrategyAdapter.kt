package com.appdev.jphil.basketballcoach.strategy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.appdev.jphil.basketballcoach.R

class StrategyAdapter(
    private val dataModels: List<StrategyDataModel>,
    private val out: StrategyContract.Adapter.Out
) : RecyclerView.Adapter<StrategyAdapter.ViewHolder>() {

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
    }

    class SeekbarViewHolder(view: View) : ViewHolder(view) {
        val lower: TextView = view.findViewById(R.id.lower)
        val higher: TextView = view.findViewById(R.id.higher)
        val seekbar: SeekBar = view.findViewById(R.id.seekbar)
    }

    class ToggleViewHolder(view: View) : ViewHolder(view) {
        val toggleButton: ToggleButton = view.findViewById(R.id.toggle_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == 0) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_strategy, parent, false)
            SeekbarViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_strategy_toggle, parent, false)
            ToggleViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataModels[position].type.isToggle) {
            1
        } else {
            0
        }
    }

    override fun getItemCount(): Int {
        return dataModels.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = dataModels[position]
        viewHolder.title.text = data.title

        if (viewHolder is SeekbarViewHolder) {
            viewHolder.higher.text = data.higher
            viewHolder.lower.text = data.lower
            viewHolder.seekbar.progress = data.value
            viewHolder.seekbar.max = data.max

            viewHolder.seekbar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        data.value = progress
                        when (data.type) {
                            StrategyType.PACE -> out.onPaceChanged(progress)
                            StrategyType.AGGRESSION -> out.onAggressionChanged(progress)
                            StrategyType.OFFENSE_FAVORS_THREES -> out.onOffenseFavorsThreesChanged(progress)
                            StrategyType.DEFENSE_FAVORS_THREES -> out.onDefenseFavorsThreesChanged(progress)
                            StrategyType.PRESS_FREQUENCY -> out.onPressFrequencyChanged(progress)
                            StrategyType.PRESS_AGGRESSION -> out.onPressAggressionChanged(progress)
                            else -> {}
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                }
            )
        } else if (viewHolder is ToggleViewHolder) {
            viewHolder.toggleButton.apply {
                isChecked = data.isEnabled
                setOnCheckedChangeListener { button, isChecked ->
                    when (data.type) {
                        StrategyType.INTENTIONAL_FOUL -> {
                            out.onIntentionallyFoulToggled(isChecked)
                            data.isEnabled = isChecked
                        }
                        StrategyType.MOVE_QUICKLY -> {
                            if (canToggleTimeManagement(isChecked)) {
                                out.onMoveQuicklyToggled(isChecked)
                                data.isEnabled = isChecked
                            } else {
                                button.isChecked = false
                            }
                        }
                        StrategyType.WASTE_TIME -> {
                            if (canToggleTimeManagement(isChecked)) {
                                out.onWasteTimeToggled(isChecked)
                                data.isEnabled = isChecked
                            } else {
                                button.isChecked = false
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun canToggleTimeManagement(isChecked: Boolean): Boolean {
        if (!isChecked) {
            return true
        }
        dataModels.forEach { model ->
            if ((model.type == StrategyType.MOVE_QUICKLY || model.type == StrategyType.WASTE_TIME) && model.isEnabled) {
                return false
            }
        }
        return true
    }
}
