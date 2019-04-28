package com.appdev.jphil.basketballcoach.playeroverview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.player.GameStatsEntity
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class PlayerOverviewFragment : Fragment(), PlayerOverviewContract.View {

    var playerId = 0
    @Inject
    lateinit var presenter: PlayerOverviewContract.Presenter
    private lateinit var recyclerView: RecyclerView
    private var attributeAdapter: PlayerAttributeAdapter? = null
    private var statsAdapter: PlayerStatsAdapter? = null
    private lateinit var playerName: TextView
    private lateinit var statsSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            playerId = it.getInt(PLAYER, 0)
        }
    }

    override fun onResume() {
        super.onResume()
        AndroidSupportInjection.inject(this)
        view?.findViewById<Spinner>(R.id.top_spinner)?.onItemSelectedListener = presenter
        presenter.onViewAttached(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_player_overview, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        playerName = view.findViewById(R.id.player_name)

        val topSpinner = view.findViewById<Spinner>(R.id.top_spinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.player_overview,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            topSpinner.adapter = adapter
        }

        statsSpinner = view.findViewById(R.id.stats_spinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.player_stats,
            android.R.layout.simple_spinner_dropdown_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            statsSpinner.adapter = adapter
        }
        statsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) { }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                statsAdapter?.statType = when (position) {
                    0 -> PlayerStatsAdapter.OFFENSIVE
                    1 -> PlayerStatsAdapter.DEFENSIVE
                    else -> PlayerStatsAdapter.OTHER
                }
                statsAdapter?.notifyDataSetChanged()
            }
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(PLAYER, playerId)
        super.onSaveInstanceState(outState)
    }

    override fun addPlayerInfo(player: Player) {
        attributeAdapter = PlayerAttributeAdapter(player, resources)
        recyclerView.adapter = attributeAdapter
        playerName.text = resources.getString(R.string.colon, player.fullName, player.getOverallRating().toString())
    }

    override fun addPlayerStats(stats: List<GameStatsEntity>) {
        statsAdapter = PlayerStatsAdapter(resources, stats)
    }

    override fun displayPlayerInfo() {
        recyclerView.adapter = attributeAdapter
        statsSpinner.visibility = View.GONE
    }

    override fun displayPlayerStats() {
        recyclerView.adapter = statsAdapter
        statsSpinner.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance(playerId: Int): PlayerOverviewFragment {
            return PlayerOverviewFragment().apply {
                this.playerId = playerId
            }
        }

        private const val PLAYER = "playerId"
    }
}