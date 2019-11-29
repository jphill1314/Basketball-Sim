package com.appdev.jphil.basketballcoach.game


import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.appdev.jphil.basketball.game.Game
import com.appdev.jphil.basketball.game.extensions.makeUserSubsIfPossible
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketball.plays.FreeThrows
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.game.GameEventEntity
import com.appdev.jphil.basketballcoach.databinding.FragmentGameBinding
import com.appdev.jphil.basketballcoach.game.adapters.GameAdapter
import com.appdev.jphil.basketballcoach.game.adapters.GameStatsAdapter
import com.appdev.jphil.basketballcoach.game.adapters.GameTeamStatsAdapter
import com.appdev.jphil.basketballcoach.main.NavigationManager
import com.appdev.jphil.basketballcoach.main.ViewModelFactory
import com.appdev.jphil.basketballcoach.strategy.StrategyAdapter
import com.appdev.jphil.basketballcoach.strategy.StrategyDataModel
import com.appdev.jphil.basketballcoach.util.TimeUtil
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class GameFragment : Fragment(), SeekBar.OnSeekBarChangeListener {

    private var viewId = 0
    private var rosterViewId = 0
    private var simSpeed = 50
    private var deadBall = true
    private lateinit var gameAdapter: GameAdapter
    private var strategyAdapter: StrategyAdapter? = null
    private var homeStatsAdapter: GameStatsAdapter? = null
    private var awayStatsAdapter: GameStatsAdapter? = null
    private val teamStatsAdapter = GameTeamStatsAdapter()

    private val args: GameFragmentArgs by navArgs()
    private lateinit var binding: FragmentGameBinding

    private var nullGame: Game? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var viewModel: GameViewModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        (activity as? NavigationManager)?.disableNavigation()
    }

    override fun onDetach() {
        (activity as? NavigationManager)?.enableNavigation()
        super.onDetach()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameBinding.inflate(inflater)

        savedInstanceState?.let {
            viewId = it.getInt("viewId", 0)
            rosterViewId = it.getInt("rosterViewId", 0)
            deadBall = it.getBoolean("deadBall", true)
            simSpeed = it.getInt("simSpeed", 50)
        }

        gameAdapter = GameAdapter(resources)
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

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.game_roster_views,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.rosterSpinner.adapter = adapter
        }
        binding.rosterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectRosterView(position)
            }
        }

        binding.homeName.text = args.homeTeamName
        binding.awayName.text = args.awayTeamName

        binding.seekBar.apply {
            setOnSeekBarChangeListener(this@GameFragment)
            progress = simSpeed
        }

        binding.playFab.setOnClickListener { onPlayFabClicked() }
        binding.timeoutFab.setOnClickListener { onTimeOutFabClicked() }

        selectView(viewId)

        if (deadBall) {
            onDeadBall()
        } else {
            onPlayFabClicked()
        }

        (activity as? NavigationManager)?.setToolbarTitle(resources.getString(R.string.game))

        return binding.root
    }
    override fun onResume() {
        super.onResume()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)
        viewModel?.gameId = args.gameId
        viewModel?.simulateGame(
            { game, newEvents -> updateGame(game, newEvents) },
            { newEvents ->  notifyNewHalf(newEvents) }
        )

        val userIsHomeTeam = args.isUserHomeTeam
        homeStatsAdapter = GameStatsAdapter(userIsHomeTeam, mutableListOf(), resources, viewModel!!) { player -> showPlayerAttributeDialog(player)}
        awayStatsAdapter = GameStatsAdapter(!userIsHomeTeam, mutableListOf(), resources, viewModel!!) { player -> showPlayerAttributeDialog(player)}
    }

    private fun updateGame(game: Game, newEvents: List<GameEventEntity>) {
        if (game.deadball && !game.madeShot && game.gamePlays.isNotEmpty() && game.gamePlays.last() !is FreeThrows && newEvents.isNotEmpty()) {
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

        homeStatsAdapter?.updatePlayerStats(game.homeTeam.players)
        awayStatsAdapter?.updatePlayerStats(game.awayTeam.players)

        teamStatsAdapter.updateTeamStats(game.homeTeam, game.awayTeam)
        teamStatsAdapter.notifyDataSetChanged()

        if (strategyAdapter == null && viewModel != null) {
            strategyAdapter = StrategyAdapter(StrategyDataModel.generateDataModels(viewModel!!.coach, resources, true), viewModel!!)
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
                selectRosterView(if (game.homeTeam.isUser) 1 else 2)
                this@GameFragment.nullGame = game
                return false
            }
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        viewModel?.pauseSim()
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
        binding.recyclerView.adapter = when (viewId) {
            1 -> homeStatsAdapter
            2 -> awayStatsAdapter
            3 -> teamStatsAdapter
            4 -> strategyAdapter
            else -> gameAdapter
        }

        binding.rosterSpinner.visibility = if (binding.recyclerView.adapter is GameStatsAdapter) {
            View.VISIBLE
        } else {
            View.GONE
        }

        selectRosterView(0)
        binding.rosterSpinner.setSelection(rosterViewId)
    }

    private fun selectRosterView(viewId: Int) {
        rosterViewId = viewId
        homeStatsAdapter?.rosterViewId = rosterViewId
        homeStatsAdapter?.notifyDataSetChanged()
        awayStatsAdapter?.rosterViewId = rosterViewId
        awayStatsAdapter?.notifyDataSetChanged()
    }

    private fun onPlayFabClicked() {
        if (handleFoulOuts(nullGame)) {
            binding.playFab.apply {
                isEnabled = false
                hide()
            }
            viewModel?.pauseGame = false
            deadBall = false

            binding.timeoutFab.show()
        }
    }

    private fun onDeadBall() {
        binding.playFab.apply {
            isEnabled = true
            show()
        }
        viewModel?.pauseGame = true
        deadBall = true

        binding.timeoutFab.hide()
    }

    private fun onTimeOutFabClicked() {
        viewModel?.callTimeout()
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
