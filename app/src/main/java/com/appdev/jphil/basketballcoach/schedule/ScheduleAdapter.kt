package com.appdev.jphil.basketballcoach.schedule

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.Game
import com.appdev.jphil.basketballcoach.R
import kotlinx.android.synthetic.main.list_item_schedule.view.*

class ScheduleAdapter(private val resources: Resources) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    var games: List<Game>? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val homeName: TextView = view.home_name
        val awayName: TextView = view.away_name
        val homeScore: TextView = view.home_score
        val awayScore: TextView = view.away_score
        val gameStatus: TextView = view.game_status
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return games?.size ?: 0
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        games?.let {
            val game = it[position]
            viewHolder.homeName.text = game.homeTeam.name
            viewHolder.awayName.text = game.awayTeam.name
            viewHolder.homeScore.text = game.homeScore.toString()
            viewHolder.awayScore.text = game.awayScore.toString()
            viewHolder.gameStatus.text = if (game.isFinal) resources.getString(R.string.game_final) else resources.getString(R.string.game_number, position + 1)
        }
    }
}