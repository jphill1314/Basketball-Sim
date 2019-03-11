package com.appdev.jphil.basketballcoach.game


import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.plays.FreeThrows
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.game.GameEventEntity
import com.appdev.jphil.basketballcoach.game.adapters.GameAdapter
import com.appdev.jphil.basketballcoach.game.adapters.GameStatsAdapter
import com.appdev.jphil.basketballcoach.game.adapters.GameTeamStatsAdapter
import com.appdev.jphil.basketballcoach.util.TimeUtil
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_game.view.*
import javax.inject.Inject

class GameFragment : Fragment(), SeekBar.OnSeekBarChangeListener {

    private var gameId: Int = 0
    private var viewId = 0
    private var rosterViewId = 0
    private var simSpeed = 50
    private var userIsHomeTeam = false
    private var deadBall = true
    private var homeTeamName = "error"
    private var awayTeamName = "error"
    private lateinit var gameAdapter: GameAdapter
    private var homeStatsAdapter: GameStatsAdapter? = null
    private var awayStatsAdapter: GameStatsAdapter? = null
    private val teamStatsAdapter = GameTeamStatsAdapter(emptyList())

    private val homeScore: TextView by lazy { view!!.home_score }
    private val awayScore: TextView by lazy { view!!.away_score }
    private val homeFouls: TextView by lazy { view!!.home_fouls }
    private val awayFouls: TextView by lazy { view!!.away_fouls }
    private val gameStatus: TextView by lazy { view!!.game_half }
    private val gameTime: TextView by lazy { view!!.game_time }
    private lateinit var fab: FloatingActionButton
    private lateinit var rosterSpinner: Spinner
    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var viewModelFactory: GameViewModelFactory
    private var viewModel: GameViewModel? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        (activity as? NavigationManager)?.disableNavigation()
    }

    override fun onDetach() {
        (activity as? NavigationManager)?.enableNavigation()
        super.onDetach()
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
            rosterViewId = it.getInt("rosterViewId", 0)
            userIsHomeTeam = it.getBoolean("userIsHome", false)
            deadBall = it.getBoolean("deadBall", true)
            simSpeed = it.getInt("simSpeed", 50)
            if (gameId == 0) {
                gameId = it.getInt("gameId", 0)
            }
        }

        gameAdapter = GameAdapter(resources)
        recyclerView = view.recycler_view
        recyclerView.layoutManager = LinearLayoutManager(context)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.game_views,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            view.spinner.adapter = adapter
        }
        view.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectView(position)
            }
        }

        rosterSpinner = view.findViewById(R.id.roster_spinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.game_roster_views,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            rosterSpinner.adapter = adapter
        }
        rosterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectRosterView(position)
            }
        }

        view.home_name.text = homeTeamName
        view.away_name.text = awayTeamName

        view.seek_bar.setOnSeekBarChangeListener(this)
        view.seek_bar.progress = simSpeed

        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener { onFabClicked() }

        selectView(viewId)

        if (deadBall) {
            onDeadBall()
        } else {
            onFabClicked()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
        viewModel?.gameId = gameId
        viewModel?.simulateGame(
            { game, newEvents -> updateGame(game, newEvents) },
            { notifyNewHalf() }
        )

        homeStatsAdapter = GameStatsAdapter(userIsHomeTeam, mutableListOf(), resources, viewModel!!)
        awayStatsAdapter = GameStatsAdapter(!userIsHomeTeam, mutableListOf(), resources, viewModel!!)
    }

    private fun updateGame(game: Game, newEvents: List<GameEventEntity>) {
        if (game.deadball && !game.madeShot && game.gamePlays.isNotEmpty() && game.gamePlays.last() !is FreeThrows) {
            onDeadBall()
        }

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

        gameAdapter.addEvents(newEvents)
        gameAdapter.notifyDataSetChanged()

        homeStatsAdapter?.updatePlayerStats(game.homeTeam.players)
        awayStatsAdapter?.updatePlayerStats(game.awayTeam.players)

        teamStatsAdapter.stats = game.getTeamStats()
        teamStatsAdapter.notifyDataSetChanged()
    }

    private fun notifyNewHalf() {
        onDeadBall()
    }

    override fun onPause() {
        super.onPause()
        viewModel?.pauseSim()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("homeTeam", homeTeamName)
        outState.putString("awayTeam", awayTeamName)
        outState.putInt("gameId", gameId)
        outState.putInt("viewId", viewId)
        outState.putInt("rosterViewId", rosterViewId)
        outState.putInt("simSpeed", simSpeed)
        outState.putBoolean("userIsHome", userIsHomeTeam)
        outState.putBoolean("deadBall", deadBall)

        super.onSaveInstanceState(outState)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        simSpeed = progress
        viewModel?.simSpeed = (100 - simSpeed) * 20L
        if (!deadBall) {
            viewModel?.pauseGame = simSpeed == 0
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        // no op
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

    private fun selectView(id: Int) {
        viewId = id
        recyclerView.adapter = when (viewId) {
            1 -> homeStatsAdapter
            2 -> awayStatsAdapter
            3 -> teamStatsAdapter
            else -> gameAdapter
        }

        rosterSpinner.visibility = if (recyclerView.adapter is GameStatsAdapter) {
            View.VISIBLE
        } else {
            View.GONE
        }

        selectRosterView(0)
        rosterSpinner.setSelection(rosterViewId)
    }

    private fun selectRosterView(viewId: Int) {
        rosterViewId = viewId
        homeStatsAdapter?.rosterViewId = rosterViewId
        awayStatsAdapter?.rosterViewId = rosterViewId
    }

    private fun onFabClicked() {
        fab.isEnabled = false
        fab.hide()
        viewModel?.pauseGame = false
        deadBall = false
    }

    private fun onDeadBall() {
        fab.isEnabled = true
        fab.show()
        viewModel?.pauseGame = true
        deadBall = true
    }

    companion object {
        fun newInstance(gameId: Int, homeTeamName: String, awayTeamName: String, userIsHomeTeam: Boolean): GameFragment {
            return GameFragment().apply {
                this.gameId = gameId
                this.homeTeamName = homeTeamName
                this.awayTeamName = awayTeamName
                this.userIsHomeTeam = userIsHomeTeam
            }
        }
    }

    interface NavigationManager {
        fun disableNavigation()
        fun enableNavigation()
    }
}
