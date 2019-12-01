package com.appdev.jphil.basketballcoach.game.preview

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.ListItemGamePreviewBinding
import kotlin.math.max

class GamePreviewAdapter(
    resources: Resources,
    private val onClick: (player: Player) -> Unit
) : RecyclerView.Adapter<GamePreviewAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListItemGamePreviewBinding): RecyclerView.ViewHolder(binding.root)

    private val homePlayers = mutableListOf<Player>()
    private val awayPlayers = mutableListOf<Player>()

    private val positions = resources.getStringArray(R.array.position_abbreviation)
    private val years = resources.getStringArray(R.array.years)

    fun setPlayers(homeTeam: Team, awayTeam: Team) {
        homePlayers.clear()
        homePlayers.addAll(homeTeam.roster)
        awayPlayers.clear()
        awayPlayers.addAll(awayTeam.roster)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ListItemGamePreviewBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return 2 * max(homePlayers.size, awayPlayers.size)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val index = position / 2
        if (position % 2 == 0) {
            if (index < homePlayers.size) {
                bind(holder.binding, homePlayers[index])
            } else {
                bindEmpty(holder.binding)
            }
        } else {
            if (index < awayPlayers.size) {
                bind(holder.binding, awayPlayers[index])
            } else {
                bindEmpty(holder.binding)
            }
        }
    }

    private fun bind(binding: ListItemGamePreviewBinding, player: Player) {
        binding.apply {
            name.text = player.lastName
            position.text = positions[player.position - 1]
            rating.text = player.getOverallRating().toString()
            year.text = years[player.year]
            root.setOnClickListener { onClick(player) }
        }
    }

    private fun bindEmpty(binding: ListItemGamePreviewBinding) {
        binding.apply {
            name.text = ""
            position.text = ""
            rating.text = ""
            year.text = ""
            root.setOnClickListener {  }
        }
    }
}