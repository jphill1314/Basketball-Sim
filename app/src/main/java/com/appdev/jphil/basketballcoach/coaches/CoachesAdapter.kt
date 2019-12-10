package com.appdev.jphil.basketballcoach.coaches

import android.content.res.Resources
import android.graphics.Typeface
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketballcoach.R

class CoachesAdapter(
    private val coaches: List<Coach>,
    private val resources: Resources,
    private val onClick: (coachId: Int) -> Unit
): RecyclerView.Adapter<CoachesAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val position: TextView = view.findViewById(R.id.position)
        val name: TextView = view.findViewById(R.id.name)
        val rating: TextView = view.findViewById(R.id.rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_coach, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return coaches.size + 1
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (position == 0) {
            viewHolder.position.text = resources.getString(R.string.position)
            viewHolder.name.text = resources.getString(R.string.name)
            viewHolder.rating.text = resources.getString(R.string.rating)
            viewHolder.position.setTypeface(null, Typeface.BOLD)
            viewHolder.name.setTypeface(null, Typeface.BOLD)
            viewHolder.rating.setTypeface(null, Typeface.BOLD)
            viewHolder.itemView.setOnClickListener(null)
        } else {
            val coach = coaches[position - 1]
            viewHolder.name.text = coach.fullName
            viewHolder.position.text = resources.getStringArray(R.array.coach_positions)[coach.type.type]
            viewHolder.rating.text = coach.getRating().toString()
            viewHolder.position.setTypeface(null, Typeface.NORMAL)
            viewHolder.name.setTypeface(null, Typeface.NORMAL)
            viewHolder.rating.setTypeface(null, Typeface.NORMAL)
            viewHolder.itemView.setOnClickListener { onClick(coach.id ?: 0) }
        }
    }
}