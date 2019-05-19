package com.appdev.jphil.basketballcoach.game.adapters

import android.content.res.Resources
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.game.GameEventEntity
import com.appdev.jphil.basketballcoach.util.TimeUtil

class GameAdapter(private val resources: Resources): RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val gameTime: TextView = view.findViewById(R.id.game_time)
        val gameEvent: TextView = view.findViewById(R.id.game_event)
        val gameScore: TextView = view.findViewById(R.id.game_score)
    }

    private val plays = mutableListOf<GameEventEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_game_event, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return plays.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val play = plays[position]
        val previousPlay = if (position < plays.size - 1) plays[position + 1] else null
        viewHolder.gameTime.text = TimeUtil.getFormattedTime(play.timeRemaining, play.shotClock)
        viewHolder.gameEvent.text = play.event

        val score = SpannableStringBuilder(resources.getString(R.string.score_dash, play.homeTeam, play.homeScore, play.awayScore, play.awayTeam))
        val midPoint = score.indexOf("-")
        if (previousPlay != null && play.homeScore != previousPlay.homeScore) {
            score.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, midPoint, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        if (previousPlay != null && play.awayScore != previousPlay.awayScore) {
            score.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), midPoint + 1, score.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        viewHolder.gameScore.text = score

        viewHolder.itemView.setBackgroundColor(if (play.homeTeamHasBall) {
            ResourcesCompat.getColor(resources, R.color.white, null)
        } else {
            ResourcesCompat.getColor(resources, R.color.gray, null)
        })
    }

    fun addEvents(newEvents: List<GameEventEntity>) {
        plays.addAll(0, newEvents.reversed())
    }
}