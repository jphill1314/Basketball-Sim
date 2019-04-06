package com.appdev.jphil.basketballcoach.tournament.round

import android.content.res.Resources
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketball.datamodels.TournamentDataModel
import com.appdev.jphil.basketballcoach.tournament.TournamentContract
import com.appdev.jphil.basketballcoach.util.Pixels

class TournamentAdapter(
    private val resources: Resources,
    private val games: MutableList<TournamentDataModel>,
    private val presenter: TournamentContract.Presenter,
    private var currentRound: Int
) : RecyclerView.Adapter<TournamentViewHolder>() {

    private val defaultSize = 100
    private val tvSize = Pixels.dpToPx(25f, resources).toInt()
    private val margin = Pixels.dpToPx(8f, resources)

    private val showButtons = MutableList(games.size) { false }

    fun updateRound(newRound: Int) {
        currentRound = newRound
        notifyDataSetChanged()
    }

    fun updateGames(newGames: List<TournamentDataModel>) {
        games.clear()
        games.addAll(newGames)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentViewHolder {
        val view = if (viewType == 1 || viewType == 3) {
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_tournament_buttons, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_tournament_no_buttons, parent, false)
        }

        view.layoutParams.width = parent.width / 2 - margin.toInt()
        if (viewType == 1 || viewType == 0) {
            val margins = view.layoutParams as ViewGroup.MarginLayoutParams
            margins.bottomMargin = Pixels.dpToPx(defaultSize.toFloat(), resources).toInt() * 2 + margin.toInt() * 3
            view.layoutParams = margins
        }

        return if (viewType == 1 || viewType == 3) {
            TournamentViewHolder.ButtonViewHolder(view, tvSize)
        } else {
            TournamentViewHolder.NoButtonViewHolder(view, tvSize)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (games[0].round == 1 && position == 0) {
            if (showButtons[position] && !games[position].isFinal) 1 else 0
        } else {
            if (showButtons[position] && !games[position].isFinal) 3 else 2
        }
    }

    override fun getItemCount(): Int {
        return games.size
    }

    override fun onBindViewHolder(viewHolder: TournamentViewHolder, position: Int) {
        val game = games[position]
        viewHolder.homeName.text = game.homeTeamName
        viewHolder.awayName.text = game.awayTeamName

        if (game.isFinal) {
            viewHolder.homeScore.text = game.homeTeamScore.toString()
            viewHolder.awayScore.text = game.awayTeamScore.toString()
        } else if (game.inProgress) {
            viewHolder.homeScore.text = game.homeTeamScore.toString()
            viewHolder.awayScore.text = game.awayTeamScore.toString()
        } else {
            viewHolder.homeScore.text = ""
            viewHolder.awayScore.text = ""
        }

        if (!game.isFinal && game.gameId != TournamentDataModel.NO_GAME_ID) {
            viewHolder.itemView.setOnClickListener {
                showButtons[position] = !showButtons[position]
                notifyDataSetChanged()
            }
        }

        if (viewHolder is TournamentViewHolder.NoButtonViewHolder) {
            when {
                game.isFinal -> viewHolder.gameStatus.text = resources.getString(R.string.game_final)
                game.inProgress -> viewHolder.gameStatus.text = resources.getString(R.string.in_progress)
                else -> viewHolder.gameStatus.text = ""
            }
        } else if (viewHolder is TournamentViewHolder.ButtonViewHolder) {
            viewHolder.simToGame.setOnClickListener { presenter.simToGame(game.gameId) }
            viewHolder.simGame.setOnClickListener { presenter.simGame(game.gameId) }
        }

        Handler().postDelayed({ viewHolder.animate(getScale(game.round), getSize(game.round)) }, 10)
    }

    private fun getSize(adapterRound: Int): Int {
        return (Pixels.dpToPx(getScale(adapterRound) * defaultSize, resources) + getMargin(adapterRound)).toInt()
    }

    private fun getScale(adapterRound: Int): Float {
        return when (adapterRound) {
            1 -> {
                when (currentRound) {
                    1 -> 1f
                    2 -> 1f
                    3 -> 0.5f
                    else -> 0.25f
                }
            }
            2 -> {
                when (currentRound) {
                    1 -> 1f
                    2 -> 1f
                    3 -> 0.5f
                    else -> 0.125f
                }
            }
            3 -> when (currentRound) {
                1 -> 2f
                2 -> 2f
                3 -> 1f
                else -> 0.5f
            }
            else -> when (currentRound) {
                1 -> 4f
                2 -> 4f
                3 -> 2f
                else -> 1f
            }
        }
    }

    private fun getMargin(adapterRound: Int): Float {
        return when (adapterRound) {
            1, 2 -> {
                when (currentRound) {
                    1, 2 -> 0f
                    else -> -(margin / 2)
                }
            }
            currentRound -> 0f
            else -> margin
        }
    }
}