package com.appdev.jphil.basketballcoach.game.sim.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.databinding.ListItemGameTeamStatsBinding

class GameTeamStatsAdapter : RecyclerView.Adapter<GameTeamStatsAdapter.ViewHolder>() {

    private val stats = mutableListOf<TeamStatLine>()

    class ViewHolder(val binding: ListItemGameTeamStatsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ListItemGameTeamStatsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int {
        return stats.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val stat = stats[position]
        viewHolder.binding.apply {
            homeStats.text = stat.homeTeamStat
            awayStats.text = stat.awayTeamStat
            statType.text = stat.statType
        }
    }

    fun updateTeamStats(homeTeam: Team, awayTeam: Team) {
        stats.clear()
        stats.add(
            TeamStatLine(
                homeTeam.name,
                awayTeam.name,
                ""
            )
        )
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
