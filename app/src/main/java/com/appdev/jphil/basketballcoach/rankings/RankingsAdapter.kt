package com.appdev.jphil.basketballcoach.rankings

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appdev.jphil.basketballcoach.advancedmetrics.TeamStatsDataModel
import com.appdev.jphil.basketballcoach.databinding.ListItemRankingBinding
import com.appdev.jphil.basketballcoach.util.getColor
import com.appdev.jphil.basketballcoach.util.getColorCompat
import java.util.Locale

class RankingsAdapter(
    private val dataModels: List<TeamStatsDataModel>,
    private val teamId: Int,
    private val resources: Resources,
    private val onClick: (teamId: Int, confId: Int) -> Unit
) : RecyclerView.Adapter<RankingsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListItemRankingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ListItemRankingBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = dataModels.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bindTeam(dataModels[position], position + 1, holder.binding)
    }

    private fun bindTeam(team: TeamStatsDataModel, rank: Int, binding: ListItemRankingBinding) {
        binding.apply {
            this.rank.text = rank.toString()
            name.text = team.teamName
            efficiency.text = formatDouble(team.getAdjEff())
            tempo.text = formatDouble(team.adjTempo)
            offEff.text = formatDouble(team.adjOffEff)
            defEff.text = formatDouble(team.adjDefEff)

            if (team.isUser || team.teamId == teamId) {
                color.visibility = View.VISIBLE
                color.setBackgroundColor(resources.getColorCompat(team.color.getColor()))
            } else {
                color.visibility = View.INVISIBLE
            }
            if (team.teamId != -1) {
                root.setOnClickListener { onClick(team.teamId, team.confId) }
            } else {
                root.setOnClickListener(null)
            }
        }
    }

    private fun formatDouble(value: Double): String {
        return "%.2f".format(Locale.getDefault(), value)
    }
}
