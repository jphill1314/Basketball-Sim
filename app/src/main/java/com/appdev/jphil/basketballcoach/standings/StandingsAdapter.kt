package com.appdev.jphil.basketballcoach.standings

import android.content.res.Resources
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.datamodels.StandingsDataModel
import com.appdev.jphil.basketballcoach.R

class StandingsAdapter(
    private var teamId: Int,
    private val standings: List<StandingsDataModel>,
    private val presenter: StandingsContract.Presenter,
    private val resources: Resources
): RecyclerView.Adapter<StandingsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val position: TextView = view.findViewById(R.id.position)
        val name: TextView = view.findViewById(R.id.name)
        val conference: TextView = view.findViewById(R.id.conference_record)
        val overall: TextView = view.findViewById(R.id.overall_record)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_standing_entry, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return standings.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = standings[position]
        viewHolder.position.text = (position + 1).toString()
        viewHolder.name.text = data.teamName
        viewHolder.conference.text = resources.getString(R.string.standings_dash, data.conferenceWins, data.conferenceLoses)
        viewHolder.overall.text = resources.getString(R.string.standings_dash, data.totalWins, data.totalLoses)
        viewHolder.itemView.setBackgroundColor(if (data.teamId == teamId) {
            Color.GRAY
        } else {
            Color.WHITE
        })

        viewHolder.itemView.setOnClickListener { presenter.onTeamSelected(data) }
    }
}