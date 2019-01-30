package com.appdev.jphil.basketballcoach.schedule

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.Game
import com.appdev.jphil.basketballcoach.R
import kotlinx.android.synthetic.main.list_item_schedule.view.*

class ScheduleAdapter(val games: List<Game>) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.temp_text
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val game = games[position]
        viewHolder.textView.text = "Game ${position + 1} - ${game.homeTeam.name} vs. ${game.awayTeam.name}"
    }
}