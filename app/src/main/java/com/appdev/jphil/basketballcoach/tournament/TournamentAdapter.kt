package com.appdev.jphil.basketballcoach.tournament

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.schedule.ScheduleDataModel

class TournamentAdapter(
    private val resources: Resources,
    private val games: MutableList<ScheduleDataModel>,
    private val presenter: TournamentContract.Presenter
) : RecyclerView.Adapter<TournamentAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val homeName: TextView = view.findViewById(R.id.home_name)
        val awayName: TextView = view.findViewById(R.id.away_name)
        val homeScore: TextView = view.findViewById(R.id.home_score)
        val awayScore: TextView = view.findViewById(R.id.away_score)
        val gameStatus: TextView = view.findViewById(R.id.game_status)
        val simToGame: Button = view.findViewById(R.id.sim_to_game)
        val simGame: Button = view.findViewById(R.id.sim_game)
    }

    fun updateGames(newGames: List<ScheduleDataModel>) {
        games.clear()
        games.addAll(newGames)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val game = games[position]
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

        viewHolder.simToGame.setOnClickListener { presenter.simToGame(game.gameId) }
        viewHolder.simGame.setOnClickListener { presenter.simGame(game.gameId) }
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