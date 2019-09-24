package com.appdev.jphil.basketballcoach.game.adapters

import android.content.res.Resources
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.game.GameEventEntity
import com.appdev.jphil.basketballcoach.databinding.ListItemGameEventBinding
import com.appdev.jphil.basketballcoach.util.TimeUtil

class GameAdapter(private val resources: Resources): RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListItemGameEventBinding): RecyclerView.ViewHolder(binding.root)

    private val plays = mutableListOf<GameEventEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ListItemGameEventBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int {
        return plays.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val play = plays[position]
        val previousPlay = if (position < plays.size - 1) plays[position + 1] else null
        viewHolder.binding.gameTime.text = TimeUtil.getFormattedTime(play.timeRemaining, play.shotClock)
        viewHolder.binding.gameEvent.text = play.event

        val score = SpannableStringBuilder(resources.getString(R.string.score_dash, play.homeTeam, play.homeScore, play.awayScore, play.awayTeam))
        val midPoint = score.indexOf("-")
        if (previousPlay != null && play.homeScore != previousPlay.homeScore) {
            score.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, midPoint, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        if (previousPlay != null && play.awayScore != previousPlay.awayScore) {
            score.setSpan(android.text.style.StyleSpan(android.graphics.Typeface.BOLD), midPoint + 1, score.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        viewHolder.binding.gameScore.text = score

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