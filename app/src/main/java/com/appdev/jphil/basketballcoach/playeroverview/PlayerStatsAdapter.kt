package com.appdev.jphil.basketballcoach.playeroverview

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.player.GameStatsEntity

class PlayerStatsAdapter(
    private val resources: Resources,
    private val stats: List<GameStatsEntity>
) : RecyclerView.Adapter<PlayerStatsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val opponent: TextView = view.findViewById(R.id.opponent)
        val result: TextView = view.findViewById(R.id.result)
        val min: TextView = view.findViewById(R.id.min_stat)
        val twoFG: TextView = view.findViewById(R.id.twofg_stat)
        val threeFG: TextView = view.findViewById(R.id.threefg_stat)
        val ftFG: TextView = view.findViewById(R.id.ft_stat)
        val pts: TextView = view.findViewById(R.id.pts_stat)
        val ast: TextView = view.findViewById(R.id.ast_stat)
        val ob: TextView = view.findViewById(R.id.ob_stat)
        val db: TextView = view.findViewById(R.id.db_stat)
        val reb: TextView = view.findViewById(R.id.reb_stat)
        val stl: TextView = view.findViewById(R.id.stl_stat)
        val fouls: TextView = view.findViewById(R.id.fouls_stat)
        val to: TextView = view.findViewById(R.id.to_stat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_player_stats, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stats.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val stat = stats[position]
        with (viewHolder) {
            opponent.text = if (stat.isHomeGame) {
                resources.getString(R.string.vs_opp, stat.opponent)
            } else {
                resources.getString(R.string.at_opp, stat.opponent)
            }
            result.text = getResult(stat)

            if (position == itemCount - 1) {
                opponent.text = stat.opponent
                result.text = ""
            }

            min.text = String.format("%.1f", stat.timePlayed / 60.0)
            twoFG.text = resources.getString(R.string.x_slash_y, stat.twoPointMakes, stat.twoPointAttempts)
            threeFG.text = resources.getString(R.string.x_slash_y, stat.threePointMakes, stat.threePointAttempts)
            ftFG.text = resources.getString(R.string.x_slash_y, stat.freeThrowMakes, stat.freeThrowShots)
            pts.text = getPoints(stat)
            ast.text = stat.assists.toString()
            ob.text = stat.offensiveRebounds.toString()
            db.text = stat.defensiveRebounds.toString()
            reb.text = (stat.offensiveRebounds + stat.defensiveRebounds).toString()
            stl.text = stat.steals.toString()
            fouls.text = stat.fouls.toString()
            to.text = stat.turnovers.toString()
        }
    }

    private fun getPoints(stat: GameStatsEntity): String {
        val points = 3 * stat.threePointMakes + 2 * stat.twoPointMakes + stat.freeThrowMakes
        return points.toString()
    }

    private fun getResult(stat: GameStatsEntity): String {
        return with (stat) {
             if (isHomeGame) {
                if (homeScore > awayScore) {
                    resources.getString(R.string.w_score, homeScore, awayScore)
                } else {
                    resources.getString(R.string.l_score, homeScore, awayScore)
                }
            } else {
                 if (homeScore > awayScore) {
                     resources.getString(R.string.l_score, awayScore, homeScore)
                 } else {
                     resources.getString(R.string.w_score, awayScore, homeScore)
                 }
            }
        }
    }
}