package com.appdev.jphil.basketballcoach.playeroverview

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketballcoach.R

class PlayerAttributeAdapter(private val player: Player, private val resources: Resources) : RecyclerView.Adapter<PlayerAttributeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val attribute: TextView = view.findViewById(R.id.player_attribute)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_player_attribute, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return NUMBER_OF_ATTRIBUTES
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.attribute.text = when (position) {
            0 -> resources.getString(R.string.close_range_shot, player.closeRangeShot)
            1 -> resources.getString(R.string.mid_range_shot, player.midRangeShot)
            2 -> resources.getString(R.string.long_range_shot, player.longRangeShot)
            3 -> resources.getString(R.string.free_throw_shot, player.freeThrowShot)
            4 -> resources.getString(R.string.post_move, player.postMove)
            5 -> resources.getString(R.string.ball_handling, player.ballHandling)
            6 -> resources.getString(R.string.passing, player.passing)
            7 -> resources.getString(R.string.off_ball_movement, player.offBallMovement)
            8 -> resources.getString(R.string.post_defense, player.postDefense)
            9 -> resources.getString(R.string.perimeter_defense, player.perimeterDefense)
            10 -> resources.getString(R.string.on_ball_defense, player.onBallDefense)
            11 -> resources.getString(R.string.off_ball_defense, player.offBallDefense)
            12 -> resources.getString(R.string.stealing, player.stealing)
            13 -> resources.getString(R.string.rebounding, player.rebounding)
            14 -> resources.getString(R.string.stamina, player.stamina)
            else -> resources.getString(R.string.aggressiveness, player.aggressiveness)
        }
    }

    private companion object {
        const val NUMBER_OF_ATTRIBUTES = 16
    }
}