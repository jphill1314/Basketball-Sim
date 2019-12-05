package com.appdev.jphil.basketballcoach.game.sim.boxscore

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketballcoach.databinding.ViewBoxScoreBinding
import com.appdev.jphil.basketballcoach.game.GameViewModel
import java.util.*

class BoxScoreAdapter(
    resources: Resources,
    private val isUsersTeam: Boolean,
    private val viewModel: GameViewModel,
    private val onLongPress: (player: Player) -> Unit
): RecyclerView.Adapter<BoxScoreAdapter.ViewHolder>(), BoxScoreContract {

    private val statsAdapter = BoxScoreStatsAdapter(resources, this)
    private val playersAdapter = BoxScoreNamesAdapter(resources, this)

    private val players = mutableListOf<Player>()
    private var selectedPlayer: Player? = null
    private var numOfPlayers = 0

    fun updatePlayers(newPlayers: List<Player>) {
        players.clear()
        players.addAll(newPlayers)

        statsAdapter.notifyDataSetChanged()
        playersAdapter.notifyDataSetChanged()

        if (numOfPlayers != newPlayers.size) {
            numOfPlayers = newPlayers.size
            notifyDataSetChanged()
        }
    }

    class ViewHolder(val binding: ViewBoxScoreBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ViewBoxScoreBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            namesRecyclerView.apply {
                adapter = playersAdapter
                layoutManager = GridLayoutManager(
                    context,
                    statsAdapter.getNumberOfRows(),
                    RecyclerView.HORIZONTAL,
                    false
                )
            }
            statsRecyclerView.apply {
                adapter = statsAdapter
                layoutManager = GridLayoutManager(
                    context,
                    statsAdapter.getNumberOfRows(),
                    RecyclerView.HORIZONTAL,
                    false
                )
            }
        }
    }

    override fun onPlayerSelected(player: Player) {
        if (!isUsersTeam) {
            return
        }

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

        statsAdapter.notifyDataSetChanged()
        playersAdapter.notifyDataSetChanged()
    }

    override fun getPlayers() = players

    override fun getTextColor(player: Player, position: Int): Int {
        return if (player.courtIndex == position && player.id != selectedPlayer?.id) {
            Color.BLACK
        } else {
            Color.GRAY
        }
    }

    override fun onPlayerLongPressed(player: Player) = onLongPress(player)
}