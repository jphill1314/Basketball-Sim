package com.appdev.jphil.basketballcoach.recruiting

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketballcoach.R

class RecruitAdapter(private val recruits: List<Recruit>, resources: Resources): RecyclerView.Adapter<RecruitAdapter.ViewHolder>() {

    private val positionNames = resources.getStringArray(R.array.position_abbreviation)

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val position: TextView = view.findViewById(R.id.position)
        val name: TextView = view.findViewById(R.id.name)
        val rating: TextView = view.findViewById(R.id.rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_roster, parent, false) // TODO: make own layout
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recruits.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val recruit = recruits[position]
        viewHolder.position.text = positionNames[recruit.position - 1]
        viewHolder.name.text = recruit.firstName + " " + recruit.lastName
        viewHolder.rating.text = recruit.rating.toString()
    }
}