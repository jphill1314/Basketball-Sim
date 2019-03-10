package com.appdev.jphil.basketballcoach.game.adapters

import android.content.res.Resources
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.Player
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.game.GameViewModel
import java.util.*

class GameStatsAdapter(
    private val isUsersTeam: Boolean,
    private val players: MutableList<Player>,
    private val resources: Resources,
    private val viewModel: GameViewModel
): RecyclerView.Adapter<GameStatsAdapter.ViewHolder>() {

    var rosterViewId = 0
    private val positions = resources.getStringArray(R.array.position_abbreviation)

    private var selectedPlayer: Player? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val pos: TextView? = view.findViewById(R.id.player_position)
        val name: TextView? = view.findViewById(R.id.player_name)
        val stat1: TextView? = view.findViewById(R.id.stat1)
        val stat2: TextView? = view.findViewById(R.id.stat2)
        val stat3: TextView? = view.findViewById(R.id.stat3)
        val stat4: TextView? = view.findViewById(R.id.stat4)
        val header: TextView? = view.findViewById(R.id.title)
    }

    fun updatePlayerStats(newPlayers: List<Player>) {
        if (isUsersTeam) {
            if (players.isEmpty()) {
                players.addAll(newPlayers)
            } else {
                newPlayers.forEach { newPlayer ->
                    val player = players.filter { it.id == newPlayer.id }
                    if (player.isNotEmpty()) {
                        Collections.replaceAll(players, player[0], newPlayer)
                    }
                }
            }
        } else {
            players.clear()
            players.addAll(newPlayers)
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = when (viewType) {
            0 -> LayoutInflater.from(parent.context).inflate(R.layout.list_item_header, parent, false)
            else -> LayoutInflater.from(parent.context).inflate(R.layout.list_item_game_stats, parent, false)
        }
        return ViewHolder(view)
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
        when (position) {
            0 -> {
                viewHolder.header?.text = resources.getString(R.string.on_the_floor)
            }
            7 -> {
                viewHolder.header?.text = resources.getString(R.string.bench)
            }
            1, 8 -> statsHeader(viewHolder)
            else -> {
                val playerPos = when (position) {
                    in 2..6 -> position - 2
                    else -> position - 4
                }
                val player = players[playerPos]
                setPlayerTextColor(viewHolder, player, playerPos)
                viewHolder.pos?.text = positions[player.position - 1]
                viewHolder.name?.text = player.lastName
                when (rosterViewId) {
                    0 -> {
                        viewHolder.stat1?.text = player.getOverallRating().toString()
                        viewHolder.stat2?.text = String.format("%.2f", player.fatigue)
                        viewHolder.stat3?.text = (player.timePlayed / 60).toString()
                        viewHolder.stat4?.text = player.fouls.toString()
                    }
                    1 -> {
                        viewHolder.stat1?.text = resources.getString(R.string.x_slash_y, player.twoPointMakes, player.twoPointAttempts)
                        viewHolder.stat2?.text = resources.getString(R.string.x_slash_y, player.threePointMakes, player.threePointAttempts)
                        viewHolder.stat3?.text = resources.getString(R.string.x_slash_y, player.freeThrowMakes, player.freeThrowShots)
                        viewHolder.stat4?.text = "0"
                    }
                    2 -> {
                        viewHolder.stat1?.text = player.offensiveRebounds.toString()
                        viewHolder.stat2?.text = player.defensiveRebounds.toString()
                        viewHolder.stat3?.text = "0"
                        viewHolder.stat4?.text = player.turnovers.toString()
                    }
                }

                if (isUsersTeam) {
                    viewHolder.itemView.setOnClickListener { onPlayerSelected(player) }
                }
            }
        }
    }

    private fun statsHeader(viewHolder: ViewHolder) {
        viewHolder.pos?.text = resources.getString(R.string.pos)
        viewHolder.name?.text = resources.getString(R.string.name)
        when (rosterViewId) {
            0 -> {
                viewHolder.stat1?.text = resources.getString(R.string.rating)
                viewHolder.stat2?.text = resources.getString(R.string.condition)
                viewHolder.stat3?.text = resources.getString(R.string.minutes)
                viewHolder.stat4?.text = resources.getString(R.string.player_fouls)
            }
            1 -> {
                viewHolder.stat1?.text = resources.getString(R.string.two_fg)
                viewHolder.stat2?.text = resources.getString(R.string.three_fg)
                viewHolder.stat3?.text = resources.getString(R.string.ft_fg)
                viewHolder.stat4?.text = resources.getString(R.string.assists)
            }
            2 -> {
                viewHolder.stat1?.text = resources.getString(R.string.ob)
                viewHolder.stat2?.text = resources.getString(R.string.db)
                viewHolder.stat3?.text = resources.getString(R.string.steals)
                viewHolder.stat4?.text = resources.getString(R.string.to)
            }
        }
    }

    private fun setPlayerTextColor(viewHolder: ViewHolder, player: Player, position: Int) {
        if (player.courtIndex == position) {
            viewHolder.pos?.setTextColor(Color.BLACK)
            viewHolder.name?.setTextColor(Color.BLACK)
            viewHolder.stat1?.setTextColor(Color.BLACK)
            viewHolder.stat2?.setTextColor(Color.BLACK)
            viewHolder.stat3?.setTextColor(Color.BLACK)
            viewHolder.stat4?.setTextColor(Color.BLACK)
        } else {
            viewHolder.pos?.setTextColor(Color.GRAY)
            viewHolder.name?.setTextColor(Color.GRAY)
            viewHolder.stat1?.setTextColor(Color.GRAY)
            viewHolder.stat2?.setTextColor(Color.GRAY)
            viewHolder.stat3?.setTextColor(Color.GRAY)
            viewHolder.stat4?.setTextColor(Color.GRAY)
        }
    }

    private fun onPlayerSelected(player: Player) {
        if (selectedPlayer == null) {
            selectedPlayer = player
        } else {
            if (selectedPlayer?.id != player.id) {
                val sub = Pair(players.indexOf(selectedPlayer!!), players.indexOf(player))
                Collections.swap(players, sub.first, sub.second)
                viewModel.addUserSub(sub)
            }
            selectedPlayer = null
        }
        notifyDataSetChanged()
    }
}