package com.appdev.jphil.basketballcoach.game

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.DialogPlayerOverviewBinding
import com.appdev.jphil.basketballcoach.databinding.FragmentPlayerOverviewBinding
import com.appdev.jphil.basketballcoach.playeroverview.PlayerAttributeAdapter

class PlayerOverviewDialogFragment : DialogFragment() {

    var player: Player? = null
    private lateinit var binding: DialogPlayerOverviewBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (player == null) {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogPlayerOverviewBinding.inflate(inflater, container, false)
        player?.let { setupView(it) }

        return binding.root
    }

    private fun setupView(player: Player) {
        binding.apply {
            recyclerView.apply {
                adapter = PlayerAttributeAdapter(player, resources)
                layoutManager = LinearLayoutManager(context)
            }

            header.apply {
                position.text = resources.getStringArray(R.array.position_abbreviation)[player.position - 1]
                playerName.text = player.fullName
                rating.text = resources.getString(R.string.rating_colon, player.getOverallRating())
                potential.text = resources.getString(R.string.potential_color, player.potential)
                year.text = resources.getStringArray(R.array.years)[player.year]
                type.text = resources.getStringArray(R.array.player_types)[player.type.type]

                minStats.text = String.format("%.2f", player.timePlayed / 60.0)
                ptsStat.text = getPoints(player).toString()
                astStat.text = player.assists.toString()
                rebStats.text = (player.offensiveRebounds + player.defensiveRebounds).toString()
                stlStats.text = player.steals.toString()
                foulsStats.text = player.fouls.toString()
            }
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