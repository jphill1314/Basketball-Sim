package com.appdev.jphil.basketballcoach.schedule

import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketball.datamodels.ScheduleDataModel
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.ListItemScheduleBinding

class ScheduleAdapter(
    private val resources: Resources,
    private val presenter: ScheduleContract.Presenter,
    private val isUsersSchedule: Boolean
) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    var games: List<ScheduleDataModel>? = null

    class ViewHolder(val binding: ListItemScheduleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        return ViewHolder(ListItemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return (games?.size ?: 0) + 1
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (position < games?.size ?: 0) {
            games?.let { handleGame(it[position], position, viewHolder.binding) }
        } else {
            viewHolder.binding.apply {
                homeName.text = "Conference tournament!"
                awayName.text = ""
                homeScore.text = ""
                awayScore.text = ""
                gameStatus.text = ""
                root.setOnClickListener { presenter.goToConferenceTournament() }
            }
        }
    }

    private fun handleGame(game: ScheduleDataModel, position: Int, binding: ListItemScheduleBinding) {
        binding.homeName.text = game.homeTeamName
        binding.awayName.text = game.awayTeamName
        when {
            game.isFinal -> {
                binding.homeScore.text = game.homeTeamScore.toString()
                binding.awayScore.text = game.awayTeamScore.toString()
                binding.gameStatus.text = resources.getString(R.string.game_final)
            }
            game.inProgress -> {
                binding.homeScore.text = game.homeTeamScore.toString()
                binding.awayScore.text = game.awayTeamScore.toString()
                binding.gameStatus.text = resources.getString(R.string.in_progress)
            }
            else -> {
                binding.homeScore.text = game.homeTeamRecord
                binding.awayScore.text = game.awayTeamRecord
                binding.gameStatus.text = resources.getString(R.string.game_number, position + 1)
            }
        }

        if (isUsersSchedule) {
            binding.simToGame.setOnClickListener { presenter.simulateToGame(game.gameId) }
            binding.simGame.setOnClickListener { presenter.simulateGame(game.gameId) }
            if (!game.isFinal) {
                binding.root.setOnClickListener {
                    val vis = binding.simGame.visibility
                    if (vis == View.VISIBLE) {
                        binding.simGame.visibility = View.GONE
                        binding.simToGame.visibility = View.GONE
                    } else {
                        binding.simGame.visibility = View.VISIBLE
                        binding.simToGame.visibility = View.VISIBLE
                    }
                }
            } else {
                binding.simGame.visibility = View.GONE
                binding.simToGame.visibility = View.GONE
            }
        }
    }
}