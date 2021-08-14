package com.appdev.jphil.basketballcoach.playeroverview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdev.jphil.basketball.players.Player
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.database.player.GameStatsEntity
import com.appdev.jphil.basketballcoach.main.NavigationManager
import com.appdev.jphil.basketballcoach.util.StatsUtil
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class PlayerOverviewFragment : Fragment(), PlayerOverviewContract.View {

    @Inject
    lateinit var presenter: PlayerOverviewContract.Presenter
    @Inject
    lateinit var navManager: NavigationManager
    private lateinit var recyclerView: RecyclerView
    private var attributeAdapter: PlayerAttributeAdapter? = null
    private var statsAdapter: PlayerStatsAdapter? = null
    val args: PlayerOverviewFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        navManager.disableDrawer()
    }

    override fun onResume() {
        super.onResume()
        view?.findViewById<Spinner>(R.id.top_spinner)?.onItemSelectedListener = presenter
        presenter.onViewAttached(this)
    }

    override fun onStop() {
        navManager.enableDrawer()
        presenter.onViewDetached()
        super.onStop()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_player_overview, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val topSpinner = view.findViewById<Spinner>(R.id.top_spinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.player_overview,
            R.layout.spinner_title
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_list_item)
            topSpinner.adapter = adapter
        }
        return view
    }

    override fun addPlayerInfo(player: Player, stats: StatsUtil) {
        attributeAdapter = PlayerAttributeAdapter(player, resources)
        recyclerView.adapter = attributeAdapter
        view?.apply {
            findViewById<TextView>(R.id.position).text = resources.getStringArray(R.array.position_abbreviation)[player.position - 1]
            findViewById<TextView>(R.id.player_name).text = player.fullName
            findViewById<TextView>(R.id.rating).text = resources.getString(R.string.rating_colon, player.getOverallRating())
            findViewById<TextView>(R.id.potential).text = resources.getString(R.string.potential_color, player.potential)
            findViewById<TextView>(R.id.year).text = resources.getStringArray(R.array.years)[player.year]
            findViewById<TextView>(R.id.type).text = resources.getStringArray(R.array.player_types)[player.type.type]

            findViewById<TextView>(R.id.min_stats).text = stats.getMinutesAvg()
            findViewById<TextView>(R.id.pts_stat).text = stats.getPointsAvg()
            findViewById<TextView>(R.id.ast_stat).text = stats.getAssistAvg()
            findViewById<TextView>(R.id.reb_stats).text = stats.getReboundAvg()
            findViewById<TextView>(R.id.stl_stats).text = stats.getStealAvg()
            findViewById<TextView>(R.id.fouls_stats).text = stats.getFoulAvg()
        }
    }

    override fun addPlayerStats(stats: List<GameStatsEntity>) {
        statsAdapter = PlayerStatsAdapter(resources, stats)
    }

    override fun displayPlayerInfo() {
        recyclerView.adapter = attributeAdapter
    }

    override fun displayPlayerStats() {
        recyclerView.adapter = statsAdapter
    }
}
