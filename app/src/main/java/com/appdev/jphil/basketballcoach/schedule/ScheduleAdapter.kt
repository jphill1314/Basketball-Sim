package com.appdev.jphil.basketballcoach.schedule

import android.content.res.Resources
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketball.datamodels.ScheduleDataModel
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.ListItemScheduleBinding
import com.appdev.jphil.basketballcoach.util.getColorCompat

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
        return (games?.size ?: 0) + 2
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (position < games?.size ?: 0) {
            games?.let { handleGame(it[position], position, viewHolder.binding) }
        } else {
            if (position == itemCount - 1) {
                viewHolder.binding.apply {
                    homeName.text = "Finish Season"
                    awayName.text = ""
                    homeScore.text = ""
                    awayScore.text = ""
                    gameStatus.text = ""
                    root.setOnClickListener { presenter.finishSeason() }
                    color.setBackgroundColor(Color.WHITE)
                }
            } else {
                viewHolder.binding.apply {
                    homeName.text = "Conference tournament!"
                    awayName.text = ""
                    homeScore.text = ""
                    awayScore.text = ""
                    gameStatus.text = ""
                    root.setOnClickListener { presenter.goToConferenceTournament() }
                    color.setBackgroundColor(Color.WHITE)
                }
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
                if (game.isVictory) {
                    binding.color.background = resources.getDrawable(R.drawable.circle_green, null)
                } else {
                    binding.color.background = resources.getDrawable(R.drawable.circle_red, null)
                }
            }
            game.inProgress -> {
                binding.homeScore.text = game.homeTeamScore.toString()
                binding.awayScore.text = game.awayTeamScore.toString()
                binding.gameStatus.text = resources.getString(R.string.in_progress)
                binding.color.background = resources.getDrawable(R.drawable.circle_gray, null)
            }
            else -> {
                binding.homeScore.text = game.homeTeamRecord
                binding.awayScore.text = game.awayTeamRecord
                binding.gameStatus.text = resources.getString(R.string.game_number, position + 1)
                binding.color.background = resources.getDrawable(R.drawable.circle_gray, null)
            }
        }

        if (isUsersSchedule) {
            binding.playGame.setOnClickListener { presenter.playGame(game.gameId) }
            binding.simGame.setOnClickListener { presenter.simulateGame(game.gameId) }
            if (!game.isFinal) {
                binding.root.setOnClickListener {
                    val vis = binding.simGame.visibility
                    if (vis == View.VISIBLE) {
                        binding.simGame.visibility = View.GONE
                        binding.playGame.visibility = View.GONE
                    } else {
                        binding.simGame.visibility = View.VISIBLE
                        binding.playGame.visibility = View.VISIBLE
                    }
                }
            } else {
                binding.simGame.visibility = View.GONE
                binding.playGame.visibility = View.GONE
                binding.root.setOnClickListener(null)
            }
        }
    }
}