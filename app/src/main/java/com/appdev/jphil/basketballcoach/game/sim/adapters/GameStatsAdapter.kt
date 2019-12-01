package com.appdev.jphil.basketballcoach.game.sim.adapters

import android.content.res.Resources
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.ListItemGameStatsBinding
import com.appdev.jphil.basketballcoach.databinding.ListItemHeaderBinding
import com.appdev.jphil.basketballcoach.game.GameViewModel
import java.util.*

class GameStatsAdapter(
    private val isUsersTeam: Boolean,
    private val players: MutableList<Player>,
    private val resources: Resources,
    private val viewModel: GameViewModel,
    private val onLongPress: (player: Player) -> Unit
): RecyclerView.Adapter<GameStatsAdapter.ViewHolder>() {

    var rosterViewId = 0
    private val positions = resources.getStringArray(R.array.position_abbreviation)

    private var selectedPlayer: Player? = null

    abstract class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    class HeaderViewHolder(val binding: ListItemHeaderBinding): ViewHolder(binding.root)

    class GameStatsViewHolder(val bidning: ListItemGameStatsBinding): ViewHolder(bidning.root)

    fun updatePlayerStats(newPlayers: List<Player>) {
        if (isUsersTeam) {
            if (players.isEmpty()) {
                players.addAll(newPlayers)
                players.sortBy { it.subPosition }
            } else {
                newPlayers.forEach { newPlayer ->
                    players.firstOrNull { it.id == newPlayer.id }?.let { player ->
                        Collections.replaceAll(players, player, newPlayer)
                    }
                }
            }
        } else {
            players.clear()
            players.addAll(newPlayers)
            players.sortBy { it.courtIndex }
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> HeaderViewHolder(
                ListItemHeaderBinding.inflate(inflater, parent, false)
            )
            else -> GameStatsViewHolder(
                ListItemGameStatsBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0, 7 -> 0
            else -> 1
        }
    }

    override fun getItemCount(): Int {
        return if (players.isEmpty()) 0 else players.size + 4
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        when {
            viewHolder is HeaderViewHolder -> bindHeader(viewHolder.binding, position)
            viewHolder is GameStatsViewHolder -> bindStats(viewHolder.bidning, position)
        }
    }

    private fun bindHeader(binding: ListItemHeaderBinding, position: Int) {
        binding.title.text = when (position) {
            0 -> resources.getString(R.string.on_the_floor)
            else -> resources.getString(R.string.bench)
        }
    }

    private fun bindStats(binding: ListItemGameStatsBinding, position: Int) {
        when (position) {
            1, 8 -> statsHeader(binding)
            else -> {
                val playerPos = when (position) {
                    in 2..6 -> position - 2
                    else -> position - 4
                }
                val player = players[playerPos]
                setPlayerTextColor(binding, player, playerPos)
                binding.playerPosition.text = positions[player.position - 1]
                binding.playerName.text = player.lastName
                when (rosterViewId) {
                    0 -> {
                        binding.result.text = if (playerPos > 4 ) {
                            player.getRatingAtPositionNoFatigue(player.position).toString()
                        } else {
                            player.getRatingAtPositionNoFatigue(playerPos + 1).toString()
                        }
                        binding.stat2.text = String.format("%.2f", player.fatigue)
                        binding.stat3.text = (player.timePlayed / 60).toString()
                        binding.stat4.text = player.fouls.toString()
                    }
                    1 -> {
                        binding.result.text = resources.getString(R.string.x_slash_y, player.twoPointMakes, player.twoPointAttempts)
                        binding.stat2.text = resources.getString(R.string.x_slash_y, player.threePointMakes, player.threePointAttempts)
                        binding.stat3.text = resources.getString(R.string.x_slash_y, player.freeThrowMakes, player.freeThrowShots)
                        binding.stat4.text = player.assists.toString()
                    }
                    2 -> {
                        binding.result.text = player.offensiveRebounds.toString()
                        binding.stat2.text = player.defensiveRebounds.toString()
                        binding.stat3.text = player.steals.toString()
                        binding.stat4.text = player.turnovers.toString()
                    }
                }

                if (isUsersTeam) {
                    binding.root.setOnClickListener { onPlayerSelected(player) }
                }
                binding.root.setOnLongClickListener {
                    onLongPress(player)
                    true
                }
            }
        }
    }

    private fun statsHeader(binding: ListItemGameStatsBinding) {
        binding.playerPosition.text = resources.getString(R.string.pos)
        binding.playerName.text = resources.getString(R.string.name)
        when (rosterViewId) {
            0 -> {
                binding.result.text = resources.getString(R.string.rating)
                binding.stat2.text = resources.getString(R.string.condition)
                binding.stat3.text = resources.getString(R.string.minutes)
                binding.stat4.text = resources.getString(R.string.player_fouls)
            }
            1 -> {
                binding.result.text = resources.getString(R.string.two_fg)
                binding.stat2.text = resources.getString(R.string.three_fg)
                binding.stat3.text = resources.getString(R.string.ft_fg)
                binding.stat4.text = resources.getString(R.string.assists)
            }
            2 -> {
                binding.result.text = resources.getString(R.string.ob)
                binding.stat2.text = resources.getString(R.string.db)
                binding.stat3.text = resources.getString(R.string.steals)
                binding.stat4.text = resources.getString(R.string.to)
            }
        }
        setHeaderTextColor(binding)
    }

    private fun setPlayerTextColor(binding: ListItemGameStatsBinding, player: Player, position: Int) {
        if (player.courtIndex == position && player.id != selectedPlayer?.id) {
            binding.playerPosition.setTextColor(Color.BLACK)
            binding.playerName.setTextColor(Color.BLACK)
            binding.result.setTextColor(Color.BLACK)
            binding.stat2.setTextColor(Color.BLACK)
            binding.stat3.setTextColor(Color.BLACK)
            binding.stat4.setTextColor(Color.BLACK)
        } else {
            binding.playerPosition.setTextColor(Color.GRAY)
            binding.playerName.setTextColor(Color.GRAY)
            binding.result.setTextColor(Color.GRAY)
            binding.stat2.setTextColor(Color.GRAY)
            binding.stat3.setTextColor(Color.GRAY)
            binding.stat4.setTextColor(Color.GRAY)
        }
    }

    private fun setHeaderTextColor(binding: ListItemGameStatsBinding) {
        binding.playerPosition.setTextColor(Color.BLACK)
        binding.playerName.setTextColor(Color.BLACK)
        binding.result.setTextColor(Color.BLACK)
        binding.stat2.setTextColor(Color.BLACK)
        binding.stat3.setTextColor(Color.BLACK)
        binding.stat4.setTextColor(Color.BLACK)
    }

    private fun onPlayerSelected(player: Player) {
        if (selectedPlayer == null) {
            selectedPlayer = player
        } else {
            if (selectedPlayer?.id != player.id) {
                val sub = Pair(players.indexOf(selectedPlayer!!), players.indexOf(player))
                Collections.swap(players, sub.first, sub.second)
                selectedPlayer?.subPosition = sub.second
                player.subPosition = sub.first
                viewModel.addUserSub(sub)
            }
            selectedPlayer = null
        }
        notifyDataSetChanged()
    }
}