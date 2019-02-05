package com.appdev.jphil.basketballcoach.game.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketballcoach.R
import kotlinx.android.synthetic.main.list_item_game_stats.view.*

class GameStatsAdapter(var players: List<Player>): RecyclerView.Adapter<GameStatsAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.player_name
        val stats: TextView = view.player_stats
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_game_stats, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return players.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val player = players[position]
        viewHolder.name.text = player.lastName
        viewHolder.stats.text = player.getStatsAsString()
    }
}