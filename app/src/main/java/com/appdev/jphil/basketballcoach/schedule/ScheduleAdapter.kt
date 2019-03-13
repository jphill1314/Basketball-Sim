package com.appdev.jphil.basketballcoach.schedule

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.appdev.jphil.basketballcoach.R
import kotlinx.android.synthetic.main.list_item_schedule.view.*

class ScheduleAdapter(
    private val resources: Resources,
    private val presenter: ScheduleContract.Presenter,
    private val isUsersSchedule: Boolean
) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    var games: List<ScheduleDataModel>? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val homeName: TextView = view.home_name
        val awayName: TextView = view.away_name
        val homeScore: TextView = view.home_score
        val awayScore: TextView = view.away_score
        val gameStatus: TextView = view.game_status
        val simToGame: Button = view.sim_to_game
        val simGame: Button = view.sim_game
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
            viewHolder.homeName.text = game.homeTeamName
            viewHolder.awayName.text = game.awayTeamName
            if (game.isFinal) {
                viewHolder.homeScore.text = game.homeTeamScore.toString()
                viewHolder.awayScore.text = game.awayTeamScore.toString()
                viewHolder.gameStatus.text = resources.getString(R.string.game_final)
            } else if (game.inProgress){
                viewHolder.homeScore.text = game.homeTeamScore.toString()
                viewHolder.awayScore.text = game.awayTeamScore.toString()
                viewHolder.gameStatus.text = resources.getString(R.string.in_progress)
            } else {
                viewHolder.homeScore.text = game.homeTeamRecord
                viewHolder.awayScore.text = game.awayTeamRecord
                viewHolder.gameStatus.text = resources.getString(R.string.game_number, position + 1)
            }

            if (isUsersSchedule) {
                viewHolder.simToGame.setOnClickListener { presenter.simulateToGame(game.gameId) }
                viewHolder.simGame.setOnClickListener { presenter.simulateGame(game.gameId) }
                if (!game.isFinal) {
                    viewHolder.itemView.setOnClickListener {
                        val vis = viewHolder.simGame.visibility
                        if (vis == View.VISIBLE) {
                            viewHolder.simGame.visibility = View.GONE
                            viewHolder.simToGame.visibility = View.GONE
                        } else {
                            viewHolder.simGame.visibility = View.VISIBLE
                            viewHolder.simToGame.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}