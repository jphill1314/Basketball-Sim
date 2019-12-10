package com.appdev.jphil.basketballcoach.game.sim.boxscore

import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.ListItemBoxScoreBinding

class BoxScoreStatsAdapter(
    private val resources: Resources,
    private val helper: BoxScoreContract
) : RecyclerView.Adapter<BoxScorePlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoxScorePlayerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BoxScorePlayerViewHolder(
                ListItemBoxScoreBinding.inflate(inflater, parent, false)
            )
    }

    override fun getItemCount(): Int = (helper.getPlayers().size + NUM_OF_NON_PLAYERS) * NUM_OF_STATS

    override fun onBindViewHolder(holder: BoxScorePlayerViewHolder, position: Int) {
        val players = helper.getPlayers()
        val column = position / (players.size + NUM_OF_NON_PLAYERS)
        when (val row = position % (players.size + NUM_OF_NON_PLAYERS)) {
            0, 6 -> bindHeader(holder.binding, column)
            in 1..5 -> bindPlayer(holder.binding, players[row - 1], column, row - 1)
            else -> bindPlayer(holder.binding, players[row - 2], column, row - 2)
        }
    }

    private fun bindHeader(binding: ListItemBoxScoreBinding, column: Int) {
        binding.root.visibility = View.VISIBLE
        binding.text.text = when (column) {
            0 -> resources.getString(R.string.min)
            1 -> resources.getString(R.string.condition)
            2 -> resources.getString(R.string.pts)
            3 -> resources.getString(R.string.fouls_label)
            4 -> resources.getString(R.string.two_fg)
            5 -> resources.getString(R.string.three_fg)
            6 -> resources.getString(R.string.ft_fg)
            7 -> resources.getString(R.string.assists)
            8 -> resources.getString(R.string.ob)
            9 -> resources.getString(R.string.db)
            10 -> resources.getString(R.string.reb)
            11 -> resources.getString(R.string.steals)
            12 -> resources.getString(R.string.to)
            else -> ""
        }
        binding.text.setTextColor(Color.BLACK)
        binding.text.setTypeface(null, Typeface.BOLD)
    }

    private fun bindPlayer(binding: ListItemBoxScoreBinding, player: Player, column: Int, position: Int) {
        binding.root.visibility = View.VISIBLE
        binding.text.text = when (column) {
            0 -> String.format("%.2f", player.timePlayed / 60.0)
            1 -> String.format("%.2f", player.fatigue)
            2 -> getPoints(player).toString()
            3 -> player.fouls.toString()
            4 -> resources.getString(R.string.x_slash_y, player.twoPointMakes, player.twoPointAttempts)
            5 -> resources.getString(R.string.x_slash_y, player.threePointMakes, player.threePointAttempts)
            6 -> resources.getString(R.string.x_slash_y, player.freeThrowMakes, player.freeThrowShots)
            7 -> player.assists.toString()
            8 -> player.offensiveRebounds.toString()
            9 -> player.defensiveRebounds.toString()
            10 -> (player.offensiveRebounds + player.defensiveRebounds).toString()
            11 -> player.steals.toString()
            12 -> player.turnovers.toString()
            else -> ""
        }
        binding.text.setTypeface(null, Typeface.NORMAL)
        binding.text.setTextColor(helper.getTextColor(player, position))
        binding.root.setOnClickListener { helper.onPlayerSelected(player) }
        binding.root.setOnLongClickListener { helper.onPlayerLongPressed(player); true }
    }

    private fun getPoints(player: Player): Int {
        return with (player) {
            freeThrowMakes + 2 * twoPointMakes + 3 * threePointMakes
        }
    }

    fun getNumberOfRows() = helper.getPlayers().size + NUM_OF_NON_PLAYERS

    private companion object {
        const val NUM_OF_STATS = 13
        const val NUM_OF_NON_PLAYERS = 2
    }
}