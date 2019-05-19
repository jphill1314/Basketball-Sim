package com.appdev.jphil.basketballcoach.game

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.playeroverview.PlayerAttributeAdapter

class PlayerOverviewDialogFragment : DialogFragment() {

    var player: Player? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (player == null) {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_player_overview, container, false)

        player?.let { setupView(view, it) }

        return view
    }

    private fun setupView(view: View, player: Player) {
        view.apply {
            findViewById<RecyclerView>(R.id.recycler_view).apply {
                adapter = PlayerAttributeAdapter(player, resources)
                layoutManager = LinearLayoutManager(context)
            }

            findViewById<TextView>(R.id.position).text = resources.getStringArray(R.array.position_abbreviation)[player.position - 1]
            findViewById<TextView>(R.id.player_name).text = player.fullName
            findViewById<TextView>(R.id.rating).text = resources.getString(R.string.rating_colon, player.getOverallRating())
            findViewById<TextView>(R.id.potential).text = resources.getString(R.string.potential_color, player.potential)
            findViewById<TextView>(R.id.year).text = resources.getStringArray(R.array.years)[player.year]
            findViewById<TextView>(R.id.type).text = resources.getStringArray(R.array.player_types)[player.type.type]

            findViewById<TextView>(R.id.min_stats).text = String.format("%.2f", player.timePlayed / 60.0)
            findViewById<TextView>(R.id.pts_stat).text = getPoints(player).toString()
            findViewById<TextView>(R.id.ast_stat).text = player.assists.toString()
            findViewById<TextView>(R.id.reb_stats).text = (player.offensiveRebounds + player.defensiveRebounds).toString()
            findViewById<TextView>(R.id.stl_stats).text = player.steals.toString()
            findViewById<TextView>(R.id.fouls_stats).text = player.fouls.toString()
        }
    }

    override fun onStart() {
        super.onStart()
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        dialog?.window?.setLayout(metrics.widthPixels, metrics.heightPixels)
    }

    private fun getPoints(player: Player): Int {
        return with (player) {
            freeThrowMakes + 2 * twoPointMakes + 3 * threePointMakes
        }
    }
}