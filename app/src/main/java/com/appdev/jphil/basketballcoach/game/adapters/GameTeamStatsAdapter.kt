package com.appdev.jphil.basketballcoach.game.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.R

class GameTeamStatsAdapter : RecyclerView.Adapter<GameTeamStatsAdapter.ViewHolder>() {

    private val stats = mutableListOf<TeamStatLine>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val homeStats: TextView = view.findViewById(R.id.home_stats)
        val awayStats: TextView = view.findViewById(R.id.away_stats)
        val statType: TextView = view.findViewById(R.id.stat_type)
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

    fun updateTeamStats(homeTeam: Team, awayTeam: Team) {
        stats.clear()
        stats.add(TeamStatLine(homeTeam.name, awayTeam.name, ""))
        stats.add(
            TeamStatLine(
                "${homeTeam.twoPointMakes}/${homeTeam.twoPointAttempts}",
                "${awayTeam.twoPointMakes}/${awayTeam.twoPointAttempts}",
                "2FGs"
            )
        )
        stats.add(
            TeamStatLine(
                "${homeTeam.threePointMakes}/${homeTeam.threePointAttempts}",
                "${awayTeam.threePointMakes}/${awayTeam.threePointAttempts}",
                "3FGs"
            )
        )
        stats.add(
            TeamStatLine(
                "${homeTeam.freeThrowMakes}/${homeTeam.freeThrowShots}",
                "${awayTeam.freeThrowMakes}/${awayTeam.freeThrowShots}",
                "FTs"
            )
        )
        stats.add(
            TeamStatLine(
                "${homeTeam.offensiveRebounds} - ${homeTeam.defensiveFouls}",
                "${awayTeam.offensiveRebounds} - ${awayTeam.defensiveRebounds}",
                "Rebounds"
            )
        )
        stats.add(
            TeamStatLine(
                "${homeTeam.turnovers}",
                "${awayTeam.turnovers}",
                "Turnovers"
            )
        )
        stats.add(
            TeamStatLine(
                "${homeTeam.offensiveFouls} - ${homeTeam.defensiveFouls}",
                "${awayTeam.offensiveFouls} - ${awayTeam.defensiveFouls}",
                "Fouls"
            )
        )
    }
}