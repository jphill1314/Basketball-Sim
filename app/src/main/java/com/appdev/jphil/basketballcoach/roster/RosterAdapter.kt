package com.appdev.jphil.basketballcoach.roster

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketballcoach.R
import kotlinx.android.synthetic.main.list_item_roster.view.*

class RosterAdapter(val roster: MutableList<Player>, val resources: Resources) : RecyclerView.Adapter<RosterAdapter.ViewHolder>() {

    private val positions: Array<String> = resources.getStringArray(R.array.position_abbreviation)
    private val classes: Array<String> = resources.getStringArray(R.array.years)

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val startPos: TextView = view.start_pos
        val name: TextView = view.name
        val rating: TextView = view.rating
        val position: TextView = view.position
        val year: TextView = view.year
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_roster, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roster.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val player = roster[position]
        viewHolder.startPos.text = if (position < 5) positions[position] else "-"
        viewHolder.name.text = player.fullName
        viewHolder.rating.text = player.getOverallRating().toString()
        viewHolder.position.text = positions[player.position - 1]
        viewHolder.year.text = classes[2]
    }
}