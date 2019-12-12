package com.appdev.jphil.basketballcoach.game.sim

import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.game.extensions.makeUserSubsIfPossible
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.plays.FreeThrows
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.game.GameEventEntity
import com.appdev.jphil.basketballcoach.databinding.FragmentGameBinding
import com.appdev.jphil.basketballcoach.game.GameViewModel
import com.appdev.jphil.basketballcoach.game.dialogs.PlayerOverviewDialogFragment
import com.appdev.jphil.basketballcoach.game.dialogs.teamtalk.TeamTalkDialog
import com.appdev.jphil.basketballcoach.game.sim.boxscore.BoxScoreAdapter
import com.appdev.jphil.basketballcoach.game.sim.adapters.GameAdapter
import com.appdev.jphil.basketballcoach.game.sim.adapters.GameTeamStatsAdapter
import com.appdev.jphil.basketballcoach.main.NavigationManager
import com.appdev.jphil.basketballcoach.main.ViewModelFactory
import com.appdev.jphil.basketballcoach.strategy.StrategyAdapter
import com.appdev.jphil.basketballcoach.strategy.StrategyDataModel
import com.appdev.jphil.basketballcoach.util.TimeUtil
import com.appdev.jphil.basketballcoach.util.getColor
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class GameFragment : Fragment(), SeekBar.OnSeekBarChangeListener {

    private var viewId = 0
    private var rosterViewId = 0
    private var simSpeed = 50
    private var deadBall = true
    private var isInTimeout = false
    private lateinit var gameAdapter: GameAdapter
    private var strategyAdapter: StrategyAdapter? = null
    private lateinit var homeBoxScoreAdapter: BoxScoreAdapter
    private lateinit var awayBoxScoreAdapter: BoxScoreAdapter
    private val teamStatsAdapter = GameTeamStatsAdapter()

    private val args: GameFragmentArgs by navArgs()
    private lateinit var binding: FragmentGameBinding

    private var nullGame: Game? = null

    @Inject
    lateinit var navManager: NavigationManager

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: GameViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(requireParentFragment(), viewModelFactory)
            .get(GameViewModel::class.java).apply {
                homeBoxScoreAdapter = BoxScoreAdapter(resources, args.isUserHomeTeam, this) { showPlayerAttributeDialog(it) }
                awayBoxScoreAdapter = BoxScoreAdapter(resources, !args.isUserHomeTeam, this) { showPlayerAttributeDialog(it) }
                gameId = args.gameId
                gameState.observe(this@GameFragment, Observer { gameState ->
                    isInTimeout = gameState.isTimeout
                    if (gameState.isNewHalf) {
                        notifyNewHalf(gameState.newEvents)
                    } else {
                        updateGame(gameState.game, gameState.newEvents)
                    }
                })
                simulateGame()
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameBinding.inflate(inflater)

        savedInstanceState?.let {
            viewId = it.getInt("viewId", 0)
            rosterViewId = it.getInt("rosterViewId", 0)
            deadBall = it.getBoolean("deadBall", true)
            simSpeed = it.getInt("simSpeed", 50)
        }

        runBlocking {
            val game = viewModel.getGame()
            gameAdapter = GameAdapter(resources, game.homeTeam.color.getColor(), game.awayTeam.color.getColor())
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.game_views,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectView(position)
            }
        }

        binding.homeName.text = args.homeTeamName
        binding.awayName.text = args.awayTeamName

        binding.seekBar.apply {
            setOnSeekBarChangeListener(this@GameFragment)
            progress = simSpeed
        }

        binding.resumeGame.setOnClickListener { onPlayFabClicked() }
        binding.callTimeout.setOnClickListener { onTimeOutFabClicked() }
        setTimeoutButtonText()

        selectView(viewId)

        if (deadBall) {
            onDeadBall()
        } else {
            onPlayFabClicked()
        }

        return binding.root
    }
    override fun onResume() {
        super.onResume()
        navManager.disableDrawer()
    }

    private fun updateGame(game: Game, newEvents: List<GameEventEntity>) {
        if (
            game.deadball &&
            !game.madeShot &&
            game.gamePlays.isNotEmpty() &&
            game.gamePlays.last() !is FreeThrows &&
            newEvents.isNotEmpty() ||
            isInTimeout
        ) {
            onDeadBall()
        }

        with(binding) {
            gameHalf.text = if (game.isFinal) {
                resources.getString(R.string.game_final)
            } else {
                resources.getString(R.string.half_placeholder, game.half)
            }
            gameTime.text = TimeUtil.getFormattedTime(game.timeRemaining, game.shotClock)
            homeScore.text = game.homeScore.toString()
            awayScore.text = game.awayScore.toString()
            homeFouls.text = resources.getString(R.string.fouls, game.homeFouls)
            awayFouls.text = resources.getString(R.string.fouls, game.awayFouls)
            homeTimeouts.text = resources.getString(R.string.timeouts, game.homeTimeouts)
            awayTimeouts.text = resources.getString(R.string.timeouts, game.awayTimeouts)
        }

        gameAdapter.addEvents(newEvents)
        gameAdapter.notifyDataSetChanged()

        teamStatsAdapter.updateTeamStats(game.homeTeam, game.awayTeam)
        teamStatsAdapter.notifyDataSetChanged()

        homeBoxScoreAdapter.updatePlayers(game.homeTeam.players)
        awayBoxScoreAdapter.updatePlayers(game.awayTeam.players)

        if (strategyAdapter == null) {
            strategyAdapter = StrategyAdapter(StrategyDataModel.generateDataModels(
                viewModel.gameStrategyOut.coach,
                resources,
                true
            ), viewModel.gameStrategyOut)
        }

        handleFoulOuts(game)
    }

    private fun notifyNewHalf(newEvents: List<GameEventEntity>) {
        onDeadBall()
        gameAdapter.addEvents(newEvents)
        gameAdapter.notifyDataSetChanged()
    }

    private fun handleFoulOuts(nullGame: Game?): Boolean {
        nullGame?.let { game ->
            val team = if (game.homeTeam.isUser) game.homeTeam else game.awayTeam
            game.makeUserSubsIfPossible()
            if (!team.allPlayersAreEligible()) {
                Toast.makeText(context, "One of your players have fouled out!", Toast.LENGTH_LONG)
                    .show() // TODO: use resources + better message
                onDeadBall()
                selectView(if (game.homeTeam.isUser) 1 else 2)
                this@GameFragment.nullGame = game
                return false
            }
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseSim()
        navManager.enableDrawer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("viewId", viewId)
        outState.putInt("rosterViewId", rosterViewId)
        outState.putInt("simSpeed", simSpeed)
        outState.putBoolean("deadBall", deadBall)

        super.onSaveInstanceState(outState)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        simSpeed = progress
        viewModel.simSpeed = (100 - simSpeed) * 20L
        if (!deadBall) {
            viewModel.pauseGame = simSpeed == 0
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    private fun selectView(id: Int) {
        viewId = id
        binding.recyclerView.adapter = when (viewId) {
            1 -> homeBoxScoreAdapter
            2 -> awayBoxScoreAdapter
            3 -> teamStatsAdapter
            4 -> strategyAdapter
            else -> gameAdapter
        }
    }

    private fun onPlayFabClicked() {
        if (isInTimeout) {
            val dialog = TeamTalkDialog().apply {
                coach = if (nullGame?.homeTeam?.isUser == true) {
                    nullGame?.homeTeam?.getHeadCoach()
                } else {
                    nullGame?.awayTeam?.getHeadCoach()
                }
                onSelectCallback = {
                    isInTimeout = false
                    onPlayFabClicked()
                }
            }
            fragmentManager?.let { dialog.show(it, "TAG") }
        } else if (handleFoulOuts(nullGame)) {
            binding.resumeGame.isEnabled = false
            viewModel.pauseGame = false
            deadBall = false
            binding.callTimeout.isEnabled = true
            setTimeoutButtonText()
        }
    }

    private fun onDeadBall() {
        binding.resumeGame.apply {
            isEnabled = true
            text = resources.getString(R.string.resume)
        }
        viewModel.pauseGame = true
        deadBall = true
        binding.callTimeout.isEnabled = false
        setTimeoutButtonText()
    }

    private fun onTimeOutFabClicked() {
        viewModel.callTimeout()
        setTimeoutButtonText()
    }

    private fun setTimeoutButtonText() {
        binding.callTimeout.text = if (viewModel.userWantsTimeout()) {
            resources.getString(R.string.cancel_timeout)
        } else {
            resources.getString(R.string.call_timeout)
        }
    }

    private fun showPlayerAttributeDialog(player: Player) {
        val dialog = PlayerOverviewDialogFragment()
        dialog.player = player
        dialog.show(fragmentManager!!, DIALOG)
    }

    companion object {
        private const val DIALOG = "dialog"
    }
}
