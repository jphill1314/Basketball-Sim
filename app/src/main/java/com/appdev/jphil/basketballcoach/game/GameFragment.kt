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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.appdev.jphil.basketball.Game
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.game.adapters.GameAdapter
import com.appdev.jphil.basketballcoach.game.adapters.GameStatsAdapter
import com.appdev.jphil.basketballcoach.game.adapters.GameTeamStatsAdapter
import com.appdev.jphil.basketballcoach.util.TimeUtil
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_game.view.*
import javax.inject.Inject

class GameFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var gameId: Int = 0
    private var viewId = 0
    private var homeTeamName = "error"
    private var awayTeamName = "error"
    private lateinit var gameAdapter: GameAdapter
    private val homeStatsAdapter = GameStatsAdapter(emptyList())
    private val awayStatsAdapter = GameStatsAdapter(emptyList())
    private val teamStatsAdapter = GameTeamStatsAdapter(emptyList())

    private val homeScore: TextView by lazy { view!!.home_score }
    private val awayScore: TextView by lazy { view!!.away_score }
    private val homeFouls: TextView by lazy { view!!.home_fouls }
    private val awayFouls: TextView by lazy { view!!.away_fouls }
    private val gameStatus: TextView by lazy { view!!.game_half }
    private val gameTime: TextView by lazy { view!!.game_time }
    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var viewModelFactory: GameViewModelFactory
    private lateinit var viewModel: GameViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        savedInstanceState?.let {
            homeTeamName = it.getString("homeTeam") ?: "error"
            awayTeamName = it.getString("awayTeam") ?: "error"
            viewId = it.getInt("viewId", 0)
            if (gameId == 0) {
                gameId = it.getInt("gameId", 0)
            }
        }

        gameAdapter = GameAdapter(resources)
        recyclerView = view.recycler_view
        recyclerView.layoutManager = LinearLayoutManager(context)
        selectView()

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.game_views,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            view.spinner.adapter = adapter
        }
        view.spinner.onItemSelectedListener = this

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
        gameStatus.text = if (game.isFinal) {
            resources.getString(R.string.game_final)
        } else {
            resources.getString(R.string.half_placeholder, game.half)
        }
        gameTime.text = TimeUtil.getFormattedTime(game.timeRemaining, game.shotClock)
        homeScore.text = game.homeScore.toString()
        awayScore.text = game.awayScore.toString()
        homeFouls.text = resources.getString(R.string.fouls, game.homeFouls)
        awayFouls.text = resources.getString(R.string.fouls, game.awayFouls)

        gameAdapter.plays = game.gamePlays.reversed()
        gameAdapter.notifyDataSetChanged()

        homeStatsAdapter.players = game.homeTeam.roster
        homeStatsAdapter.notifyDataSetChanged()
        awayStatsAdapter.players = game.awayTeam.roster
        awayStatsAdapter.notifyDataSetChanged()

        teamStatsAdapter.stats = game.getTeamStats()
        teamStatsAdapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseSim()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("homeTeam", homeTeamName)
        outState.putString("awayTeam", awayTeamName)
        outState.putInt("gameId", gameId)
        outState.putInt("viewId", viewId)

        super.onSaveInstanceState(outState)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) { /* no op */ }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewId = position
        selectView()
    }

    private fun selectView() {
        recyclerView.adapter = when (viewId) {
            1 -> homeStatsAdapter
            2 -> awayStatsAdapter
            3 -> teamStatsAdapter
            else -> gameAdapter
        }
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
