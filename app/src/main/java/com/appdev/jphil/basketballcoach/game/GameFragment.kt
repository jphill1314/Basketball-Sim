package com.appdev.jphil.basketballcoach.game


import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.Game
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.util.TimeUtil
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_game.view.*
import javax.inject.Inject

class GameFragment : Fragment() {

    var gameId: Int = 0
    private var homeTeamName = "error"
    private var awayTeamName = "error"
    private lateinit var adapter: GameAdapter

    private val homeScore: TextView by lazy { view!!.home_score }
    private val awayScore: TextView by lazy { view!!.away_score }
    private val gameStatus: TextView by lazy { view!!.game_half }
    private val gameTime: TextView by lazy { view!!.game_time }

    @Inject
    lateinit var viewModelFactory: GameViewModelFactory
    lateinit var viewModel: GameViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)
        adapter = GameAdapter(resources)
        view.recycler_view.adapter = adapter
        view.recycler_view.layoutManager = LinearLayoutManager(context)

        savedInstanceState?.let {
            homeTeamName = it.getString("homeTeam") ?: "error"
            awayTeamName = it.getString("awayTeam") ?: "error"
            if (gameId == 0) {
                gameId = it.getInt("gameId", 0)
            }
        }

        view.home_name.text = homeTeamName
        view.away_name.text = awayTeamName
        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
        viewModel.gameId = gameId
        viewModel.simulateGame { game -> updateGame(game) }
    }

    private fun updateGame(game: Game) {
        gameStatus.text = if (game.isFinal) "Final" else "Half: ${game.half}"
        gameTime.text = TimeUtil.getFormattedTime(game.timeRemaining, game.shotClock)
        homeScore.text = game.homeScore.toString()
        awayScore.text = game.awayScore.toString()

        adapter.plays = game.gamePlays.reversed()
        adapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseSim()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("homeTeam", homeTeamName)
        outState.putString("awayTeam", awayTeamName)
        outState.putInt("gameId", gameId)

        super.onSaveInstanceState(outState)
    }

    companion object {
        fun newInstance(gameId: Int, homeTeamName: String, awayTeamName: String): GameFragment {
            return GameFragment().apply {
                this.gameId = gameId
                this.homeTeamName = homeTeamName
                this.awayTeamName = awayTeamName
            }
        }
    }
}
