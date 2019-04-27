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
        val progress = player.progression
        viewHolder.attribute.text = when (position) {
            0 -> resources.getString(R.string.close_range_shot, player.closeRangeShot) + " - ${progress?.closeRangeShot}"
            1 -> resources.getString(R.string.mid_range_shot, player.midRangeShot) + " - ${progress?.midRangeShot}"
            2 -> resources.getString(R.string.long_range_shot, player.longRangeShot) + " - ${progress?.longRangeShot}"
            3 -> resources.getString(R.string.free_throw_shot, player.freeThrowShot) + " - ${progress?.freeThrowShot}"
            4 -> resources.getString(R.string.post_move, player.postMove) + " - ${progress?.postMove}"
            5 -> resources.getString(R.string.ball_handling, player.ballHandling) + " - ${progress?.ballHandling}"
            6 -> resources.getString(R.string.passing, player.passing) + " - ${progress?.passing}"
            7 -> resources.getString(R.string.off_ball_movement, player.offBallMovement) + " - ${progress?.offBallMovement}"
            8 -> resources.getString(R.string.post_defense, player.postDefense) + " - ${progress?.postDefense}"
            9 -> resources.getString(R.string.perimeter_defense, player.perimeterDefense) + " - ${progress?.perimeterDefense}"
            10 -> resources.getString(R.string.on_ball_defense, player.onBallDefense) + " - ${progress?.onBallDefense}"
            11 -> resources.getString(R.string.off_ball_defense, player.offBallDefense) + " - ${progress?.offBallDefense}"
            12 -> resources.getString(R.string.stealing, player.stealing) + " - ${progress?.stealing}"
            13 -> resources.getString(R.string.rebounding, player.rebounding) + " - ${progress?.rebounding}"
            14 -> resources.getString(R.string.stamina, player.stamina) + " - ${progress?.stamina}"
            else -> resources.getString(R.string.aggressiveness, player.aggressiveness)
        }
    }

    private companion object {
        const val NUMBER_OF_ATTRIBUTES = 16
    }
}