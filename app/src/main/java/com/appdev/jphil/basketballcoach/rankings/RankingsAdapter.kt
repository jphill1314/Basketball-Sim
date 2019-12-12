package com.appdev.jphil.basketballcoach.rankings

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appdev.jphil.basketball.teams.TeamColor
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.advancedmetrics.TeamStatsDataModel
import com.appdev.jphil.basketballcoach.databinding.ListItemRankingBinding
import com.appdev.jphil.basketballcoach.util.getColor
import com.appdev.jphil.basketballcoach.util.getColorCompat
import java.util.*

class RankingsAdapter(
    private val dataModels: List<TeamStatsDataModel>,
    private val teamId: Int,
    private val resources: Resources,
    private val onClick: (teamId: Int, confId: Int) -> Unit
) : RecyclerView.Adapter<RankingsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListItemRankingBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ListItemRankingBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = dataModels.size + 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            bindHeader(holder.binding)
        } else {
            bindTeam(dataModels[position - 1], position, holder.binding)
        }
    }

    private fun bindHeader(binding: ListItemRankingBinding) {
        binding.apply {
            rank.text = ""
            name.text = resources.getString(R.string.team)
            efficiency.text = resources.getString(R.string.adj_eff)
            offEff.text = resources.getString(R.string.adj_off)
            defEff.text = resources.getString(R.string.adj_def)
            color.visibility = View.INVISIBLE
            root.setOnClickListener(null)
        }
    }

    private fun bindTeam(team: TeamStatsDataModel, rank: Int, binding: ListItemRankingBinding) {
        binding.apply {
            this.rank.text = rank.toString()
            name.text = resources.getString(R.string.two_strings, team.team.schoolName, team.team.mascot) + " ${team.team.rating}"
            efficiency.text = formatDouble(team.rawOffEff - team.rawDefEff)
            offEff.text = formatDouble(team.rawOffEff)
            defEff.text = formatDouble(team.rawDefEff)

            if (team.team.isUser || team.team.teamId == teamId) {
                color.visibility = View.VISIBLE
                val teamColor = TeamColor.fromInt(team.team.color)
                color.setBackgroundColor(resources.getColorCompat(teamColor.getColor()))
            } else {
                color.visibility = View.INVISIBLE
            }
            root.setOnClickListener { onClick(team.team.teamId, team.team.conferenceId) }
        }
    }

    private fun formatDouble(value: Double): String {
        return "%.2f".format(Locale.getDefault(), value)
    }
}