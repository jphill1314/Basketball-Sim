package com.appdev.jphil.basketballcoach.coachoverview

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.coach.CoachEntity

class CoachAttributeAdapter(
    private val coach: CoachEntity,
    private val resources: Resources
) : RecyclerView.Adapter<CoachAttributeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val attribute: TextView = view.findViewById(R.id.player_attribute)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_player_attribute, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 9
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.attribute.text = when (position) {
            0 -> resources.getString(R.string.recruiting_colon, coach.recruiting)
            1 -> resources.getString(R.string.shooting_colon, coach.teachShooting)
            2 -> resources.getString(R.string.post_move, coach.teachPostMoves)
            3 -> resources.getString(R.string.ball_handling, coach.teachBallControl)
            4 -> resources.getString(R.string.post_defense, coach.teachPostDefense)
            5 -> resources.getString(R.string.perimeter_defense, coach.teachPerimeterDefense)
            6 -> resources.getString(R.string.positioning_colon, coach.teachPositioning)
            7 -> resources.getString(R.string.rebounding, coach.teachRebounding)
            else -> resources.getString(R.string.conditioning_colon, coach.teachConditioning)
        }
    }
}
