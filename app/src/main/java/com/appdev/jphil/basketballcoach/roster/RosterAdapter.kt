package com.appdev.jphil.basketballcoach.roster

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketballcoach.R
import kotlinx.android.synthetic.main.list_item_header.view.*
import kotlinx.android.synthetic.main.list_item_roster.view.*

class RosterAdapter(val roster: MutableList<Player>, val resources: Resources) : RecyclerView.Adapter<RosterAdapter.ViewHolder>() {

    private val positions: Array<String> = resources.getStringArray(R.array.position_abbreviation)
    private val classes: Array<String> = resources.getStringArray(R.array.years)

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView? = view.name
        val rating: TextView? = view.rating
        val position: TextView? = view.position
        val year: TextView? = view.year
        val title: TextView? = view.title
    }

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        return if (type == 0) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_header, parent, false)
            ViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_roster, parent, false)
            ViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> 0
            6 -> 0
            else -> 1
        }
    }

    override fun getItemCount(): Int {
        return roster.size + 2
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (position == 0 || position == 6) {
            if (position == 0) {
                viewHolder.title?.text = resources.getString(R.string.starting_lineup)
            } else {
                viewHolder.title?.text = resources.getString(R.string.bench)
            }
        } else {
            val player = if (position < 6) roster[position - 1] else roster[position - 2]
            viewHolder.position?.text = if (position < 6) positions[position - 1] else positions[player.position - 1]
            viewHolder.name?.text = player.fullName
            viewHolder.rating?.text = player.getOverallRating().toString()
            viewHolder.year?.text = classes[2]

            if (position == 5) {
                val params = viewHolder.itemView.layoutParams as ViewGroup.MarginLayoutParams
                params.bottomMargin = resources.getDimension(R.dimen.sixteen_dp).toInt()
            }
        }
    }
}