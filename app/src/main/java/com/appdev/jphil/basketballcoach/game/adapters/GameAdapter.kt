package com.appdev.jphil.basketballcoach.game.adapters

import android.content.res.Resources
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.plays.BasketballPlay
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.util.TimeUtil
import kotlinx.android.synthetic.main.list_item_game_event.view.*

class GameAdapter(private val resources: Resources): RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val gameTime: TextView = view.game_time
        val gameEvent: TextView = view.game_event
    }

    var plays = listOf<BasketballPlay>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_game_event, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return plays.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val play = plays[position]
        viewHolder.gameTime.text = TimeUtil.getFormattedTime(play.timeRemaining, play.shotClock)
        viewHolder.gameEvent.text = play.playAsString

        viewHolder.itemView.setBackgroundColor(if (play.homeTeamStartsWithBall) {
            ResourcesCompat.getColor(resources, R.color.white, null)
        } else {
            ResourcesCompat.getColor(resources, R.color.gray, null)
        })
    }
}