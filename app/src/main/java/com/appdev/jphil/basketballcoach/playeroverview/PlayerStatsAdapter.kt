package com.appdev.jphil.basketballcoach.playeroverview

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.player.GameStatsEntity

class PlayerStatsAdapter(private val resources: Resources, private val stats: List<GameStatsEntity>) : RecyclerView.Adapter<PlayerStatsAdapter.ViewHolder>() {

    var statType = OFFENSIVE

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.game)
        val stat1: TextView = view.findViewById(R.id.stat1)
        val stat2: TextView = view.findViewById(R.id.stat2)
        val stat3: TextView = view.findViewById(R.id.stat3)
        val stat4: TextView = view.findViewById(R.id.stat4)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_player_stats, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return stats.size + 1
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (position == 0) {
            when (statType) {
                OFFENSIVE -> bindOffensiveHeader(viewHolder)
                DEFENSIVE -> bindDefensiveHeader(viewHolder)
                else -> bindOtherHeader(viewHolder)
            }
        } else {
            val game = stats[position - 1]
            when (statType) {
                OFFENSIVE -> bindOffensiveStats(viewHolder, game)
                DEFENSIVE -> bindDefensiveStats(viewHolder, game)
                else -> bindOtherStats(viewHolder, game)
            }
        }
    }

    private fun bindOffensiveHeader(viewHolder: ViewHolder) {
        viewHolder.name.text = resources.getString(R.string.game)
        viewHolder.stat1.text = resources.getString(R.string.two_fg)
        viewHolder.stat2.text = resources.getString(R.string.three_fg)
        viewHolder.stat3.text = resources.getString(R.string.ft_fg)
        viewHolder.stat4.text = resources.getString(R.string.assists)
    }

    private fun bindDefensiveHeader(viewHolder: ViewHolder) {
        viewHolder.name.text = resources.getString(R.string.game)
        viewHolder.stat1.text = resources.getString(R.string.ob)
        viewHolder.stat2.text = resources.getString(R.string.db)
        viewHolder.stat3.text = resources.getString(R.string.steals)
        viewHolder.stat4.text = resources.getString(R.string.to)
    }

    private fun bindOtherHeader(viewHolder: ViewHolder) {
        viewHolder.name.text = resources.getString(R.string.game)
        viewHolder.stat1.text = resources.getString(R.string.minutes)
        viewHolder.stat2.text = ""
        viewHolder.stat3.text = ""
        viewHolder.stat4.text = ""
    }

    private fun bindOffensiveStats(viewHolder: ViewHolder, game: GameStatsEntity) {
        viewHolder.name.text = game.opponent
        viewHolder.stat1.text = resources.getString(R.string.x_slash_y, game.twoPointMakes, game.twoPointAttempts)
        viewHolder.stat2.text = resources.getString(R.string.x_slash_y, game.threePointMakes, game.threePointAttempts)
        viewHolder.stat3.text = resources.getString(R.string.x_slash_y, game.freeThrowMakes, game.freeThrowShots)
        viewHolder.stat4.text = game.assists.toString()
    }

    private fun bindDefensiveStats(viewHolder: ViewHolder, game: GameStatsEntity) {
        viewHolder.name.text = game.opponent
        viewHolder.stat1.text = game.offensiveRebounds.toString()
        viewHolder.stat2.text = game.defensiveRebounds.toString()
        viewHolder.stat3.text = game.steals.toString()
        viewHolder.stat4.text = game.fouls.toString()
    }

    private fun bindOtherStats(viewHolder: ViewHolder, game: GameStatsEntity) {
        viewHolder.name.text = game.opponent
        viewHolder.stat1.text = (game.timePlayed / 60).toString()
        viewHolder.stat2.text = ""
        viewHolder.stat3.text = ""
        viewHolder.stat4.text = ""
    }

    companion object {
        const val OFFENSIVE = 1
        const val DEFENSIVE = 2
        const val OTHER = 3
    }
}