package com.appdev.jphil.basketballcoach.standings

import android.content.res.Resources
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.ListItemStandingEntryBinding
import com.appdev.jphil.basketballcoach.util.getColor
import com.appdev.jphil.basketballcoach.util.getColorCompat

class StandingsAdapter(
    private var teamId: Int,
    private val standings: List<StandingsDataModel>,
    private val presenter: StandingsContract.Presenter,
    private val resources: Resources
): RecyclerView.Adapter<StandingsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListItemStandingEntryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ListItemStandingEntryBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return standings.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = standings[position]
        viewHolder.binding.apply {
            this.position.text = (position + 1).toString()
            name.text = data.teamName
            conferenceRecord.text = resources.getString(R.string.standings_dash, data.conferenceWins, data.conferenceLoses)
            overallRecord.text = resources.getString(R.string.standings_dash, data.totalWins, data.totalLoses)
            root.setOnClickListener { presenter.onTeamSelected(data) }
            teamColor.setBackgroundColor(if (data.teamId == teamId) resources.getColorCompat(data.teamColor.getColor()) else Color.WHITE)
        }
    }
}