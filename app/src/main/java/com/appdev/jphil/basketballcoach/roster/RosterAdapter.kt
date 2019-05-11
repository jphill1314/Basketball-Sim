package com.appdev.jphil.basketballcoach.roster

import android.content.res.Resources
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketballcoach.R

class RosterAdapter(
    val presenter: RosterContract.Presenter,
    val roster: MutableList<RosterDataModel>,
    val resources: Resources
) : RecyclerView.Adapter<RosterAdapter.ViewHolder>() {

    private val positions: Array<String> = resources.getStringArray(R.array.position_abbreviation)
    private val classes: Array<String> = resources.getStringArray(R.array.years)
    var isUsersTeam = false

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView? = view.findViewById(R.id.name)
        val rating: TextView? = view.findViewById(R.id.rating)
        val position: TextView? = view.findViewById(R.id.position)
        val year: TextView? = view.findViewById(R.id.year)
        val title: TextView? = view.findViewById(R.id.title)
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
            7 -> 0
            else -> 1
        }
    }

    override fun getItemCount(): Int {
        return if (roster.isNotEmpty()) roster.size + 4 else 0
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (position == 0 || position == 7) {
            if (position == 0) {
                viewHolder.title?.text = resources.getString(R.string.starting_lineup)
            } else {
                viewHolder.title?.text = resources.getString(R.string.bench)
            }
        } else if (position == 1 || position == 8) {
            viewHolder.position?.text = resources.getString(R.string.pos)
            viewHolder.name?.text = resources.getString(R.string.name)
            viewHolder.rating?.text = resources.getString(R.string.rating)
            viewHolder.year?.text = resources.getString(R.string.year)

            val params = viewHolder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            params.bottomMargin = resources.getDimensionPixelSize(R.dimen.card_margin_bottom)
            setTextColor(viewHolder, false)
        } else {
            val dataModel = if (position < 7) roster[position - 2] else roster[position - 4]
            val player = dataModel.player
            setTextColor(viewHolder, dataModel.isSelected)

            viewHolder.position?.text = if (position < 7) positions[position - 2] else positions[player.position - 1]
            viewHolder.name?.text = player.fullName
            viewHolder.rating?.text = player.getOverallRating().toString()
            viewHolder.year?.text = classes[player.year]

            if (position == 6) {
                val params = viewHolder.itemView.layoutParams as ViewGroup.MarginLayoutParams
                params.bottomMargin = resources.getDimensionPixelSize(R.dimen.sixteen_dp)
            }

            if (isUsersTeam) {
                viewHolder.itemView.setOnClickListener {
                    presenter.onPlayerSelected(player)
                    dataModel.isSelected = !dataModel.isSelected
                    notifyDataSetChanged()
                }
            }
            viewHolder.itemView.setOnLongClickListener {
                presenter.onPlayerLongPressed(player)
                true
            }
        }
    }

    private fun setTextColor(viewHolder: ViewHolder, isSelected: Boolean) {
        val color = if (isSelected) R.color.gray else R.color.black
        viewHolder.position?.setTextColor(ResourcesCompat.getColor(resources, color, null))
        viewHolder.name?.setTextColor(ResourcesCompat.getColor(resources, color, null))
        viewHolder.rating?.setTextColor(ResourcesCompat.getColor(resources, color, null))
        viewHolder.year?.setTextColor(ResourcesCompat.getColor(resources, color, null))
    }

    fun updateRoster(roster: MutableList<RosterDataModel>) {
        this.roster.clear()
        this.roster.addAll(roster)
        notifyDataSetChanged()
    }
}