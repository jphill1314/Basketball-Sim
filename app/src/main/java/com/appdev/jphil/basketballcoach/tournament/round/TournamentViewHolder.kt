package com.appdev.jphil.basketballcoach.tournament.round

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView
import com.appdev.jphil.basketballcoach.R

abstract class TournamentViewHolder(view: View, private val tvSize: Int) : RecyclerView.ViewHolder(view) {
    val homeName: TextView = view.findViewById(R.id.home_name)
    val awayName: TextView = view.findViewById(R.id.away_name)
    val homeScore: TextView = view.findViewById(R.id.home_score)
    val awayScore: TextView = view.findViewById(R.id.away_score)
    abstract val views: List<View>

    fun animate(scale: Float, size: Int) {
        views.forEach {view ->
            getAnimation(view, (scale * tvSize).toInt())
        }
        getAnimation(itemView, size)
    }

    private fun getAnimation(view: View, newHeight: Int) {
        SlideAnimation(view.height, newHeight, view).apply {
            interpolator = LinearInterpolator()
            duration = 100
        }.also {
            view.animation = it
            view.startAnimation(it)
        }
    }


    class NoButtonViewHolder(view: View, tvSize: Int) : TournamentViewHolder(view, tvSize) {
        val gameStatus: TextView = view.findViewById(R.id.game_status)
        override val views = listOf(itemView, homeName, awayName, homeScore, awayScore, gameStatus)
    }

    class ButtonViewHolder(view: View, tvSize: Int): TournamentViewHolder(view, tvSize) {
        val playGame: Button = view.findViewById(R.id.play_game)
        val simGame: Button = view.findViewById(R.id.sim_game)
        override val views = listOf(itemView, homeName, awayName, homeScore, awayScore)
    }
}