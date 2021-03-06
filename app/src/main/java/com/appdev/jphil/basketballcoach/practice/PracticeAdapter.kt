package com.appdev.jphil.basketballcoach.practice

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.players.PlayerProgressHelper
import com.appdev.jphil.basketballcoach.R

class PracticeAdapter(
    players: List<Player>
) : RecyclerView.Adapter<PracticeAdapter.ViewHolder>() {

    private val data = PlayerProgressHelper.sortPlayersByPerformance(players)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.player_name)
        val progress: TextView = view.findViewById(R.id.practice_result)
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_practice, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = data[position]
        viewHolder.name.text = item.first
        viewHolder.progress.apply {
            text = item.second.toString()
            setTextColor(getProgressAsColor(item.second))
        }
    }

    private fun getProgressAsColor(progress: Int): Int {
        val progressAsPercent = progress / 100.0
        val green: Int = (255 * progressAsPercent).toInt()
        val red: Int = (255 * (1 - progressAsPercent)).toInt()
        return Color.rgb(red, green, 0)
    }
}