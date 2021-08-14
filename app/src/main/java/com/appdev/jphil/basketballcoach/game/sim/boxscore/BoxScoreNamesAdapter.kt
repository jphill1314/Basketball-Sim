package com.appdev.jphil.basketballcoach.game.sim.boxscore

import android.content.res.Resources
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.ListItemBoxScoreLeftBinding

class BoxScoreNamesAdapter(
    private val resources: Resources,
    private val helper: BoxScoreContract
) : RecyclerView.Adapter<BoxScoreLeftViewHolder>() {

    private val positions = resources.getStringArray(R.array.position_abbreviation)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoxScoreLeftViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BoxScoreLeftViewHolder(
            ListItemBoxScoreLeftBinding.inflate(inflater, parent, false)
        )
    }

    override fun getItemCount(): Int = helper.getPlayers().size + NUM_OF_NON_PLAYERS

    override fun onBindViewHolder(holder: BoxScoreLeftViewHolder, position: Int) {
        when (position) {
            0 -> holder.binding.apply {
                this.position.text = ""
                name.text = resources.getString(R.string.on_the_floor)
                name.setTypeface(null, Typeface.BOLD)
            }
            in 1..5 -> {
                val location = position - 1
                bindPlayer(holder.binding, helper.getPlayers()[location], location)
            }
            6 -> holder.binding.apply {
                this.position.text = ""
                name.text = resources.getString(R.string.bench)
                name.setTypeface(null, Typeface.BOLD)
            }
            else -> {
                val location = position - 2
                bindPlayer(holder.binding, helper.getPlayers()[location], location)
            }
        }
    }

    private fun bindPlayer(binding: ListItemBoxScoreLeftBinding, player: Player, position: Int) {
        binding.name.apply {
            text = player.firstInitialLastName
            setTextColor(helper.getTextColor(player, position))
            setTypeface(null, Typeface.NORMAL)
        }
        if (position < 5) {
            binding.position.text = positions[position]
            binding.rating.text = player.getRatingAtPositionNoFatigue(position + 1).toString()
        } else {
            binding.position.text = positions[player.position - 1]
            binding.rating.text = player.getOverallRating().toString()
        }

        binding.root.setOnClickListener { helper.onPlayerSelected(player) }
        binding.root.setOnLongClickListener { helper.onPlayerLongPressed(player); true }
    }

    private companion object {
        const val NUM_OF_NON_PLAYERS = 2
    }
}
