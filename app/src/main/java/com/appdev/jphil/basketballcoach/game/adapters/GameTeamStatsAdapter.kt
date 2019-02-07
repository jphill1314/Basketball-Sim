package com.appdev.jphil.basketballcoach.game.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.TeamStatLine
import com.appdev.jphil.basketballcoach.R
import kotlinx.android.synthetic.main.list_item_game_team_stats.view.*

class GameTeamStatsAdapter(var stats: List<TeamStatLine>) : RecyclerView.Adapter<GameTeamStatsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val homeStats: TextView = view.home_stats
        val awayStats: TextView = view.away_stats
        val statType: TextView = view.stat_type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_game_team_stats, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stats.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val stat = stats[position]
        viewHolder.homeStats.text = stat.homeTeamStat
        viewHolder.awayStats.text = stat.awayTeamStat
        viewHolder.statType.text = stat.statType
    }
}