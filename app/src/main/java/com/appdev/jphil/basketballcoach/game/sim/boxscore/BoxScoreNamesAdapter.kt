package com.appdev.jphil.basketballcoach.game.sim.boxscore

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.ListItemBoxScoreHeaderBinding
import com.appdev.jphil.basketballcoach.databinding.ListItemBoxScoreLeftBinding

class BoxScoreNamesAdapter(
    private val resources: Resources,
    private val helper: BoxScoreContract
) : RecyclerView.Adapter<BoxScoreViewHolder>() {

    private val positions = resources.getStringArray(R.array.position_abbreviation)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoxScoreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> HeaderViewHolder(
                ListItemBoxScoreHeaderBinding.inflate(inflater, parent, false)
            )
            else -> BoxScoreLeftViewHolder(
                ListItemBoxScoreLeftBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int) = when (position) {
        0, 7 -> 0
        1, 8 -> 1
        else -> 2
    }

    override fun getItemCount(): Int = helper.getPlayers().size + NUM_OF_NON_PLAYERS

    override fun onBindViewHolder(holder: BoxScoreViewHolder, position: Int) {
        when (position) {
            0 -> (holder as? HeaderViewHolder)?.binding?.title?.text = resources.getString(R.string.on_the_floor)
            1, 8 -> if (holder is BoxScoreLeftViewHolder) {
                bindPlayerName(holder.binding)
            }
            in 2..6 -> if (holder is BoxScoreLeftViewHolder) {
                val location = position - 2
                bindPlayer(holder.binding, helper.getPlayers()[location], location)
            }
            7 -> (holder as? HeaderViewHolder)?.binding?.title?.text = resources.getString(R.string.bench)
            else -> if (holder is BoxScoreLeftViewHolder) {
                val location = position - 4
                bindPlayer(holder.binding, helper.getPlayers()[location], location)
            }
        }
    }

    private fun bindPlayerName(binding: ListItemBoxScoreLeftBinding) {
        binding.name.apply {
            text = resources.getString(R.string.name)
            setTextColor(Color.BLACK)
        }
        binding.rating.text = ""
        binding.position.text = ""
    }

    private fun bindPlayer(binding: ListItemBoxScoreLeftBinding, player: Player, position: Int) {
        binding.name.apply {
            text = player.firstInitialLastName
            setTextColor(helper.getTextColor(player, position))
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
        const val NUM_OF_NON_PLAYERS = 4
    }
}