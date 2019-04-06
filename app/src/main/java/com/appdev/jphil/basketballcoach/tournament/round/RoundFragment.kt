package com.appdev.jphil.basketballcoach.tournament.round

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketball.datamodels.TournamentDataModel
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.tournament.TournamentContract

class RoundFragment : Fragment() {

    private lateinit var adapter: TournamentAdapter
    lateinit var dataModels: MutableList<TournamentDataModel>
    lateinit var presenter: TournamentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        adapter = TournamentAdapter(resources, dataModels, presenter, 1)
        return inflater.inflate(R.layout.fragment_round, container, false).apply {
            findViewById<RecyclerView>(R.id.recycler_view).apply {
                this.adapter = this@RoundFragment.adapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    fun addData(games: MutableList<TournamentDataModel>) {
        adapter.updateGames(games)
    }

    fun updateRound(round: Int) {
        adapter.updateRound(round)
    }

    fun getFragmentRound(): Int {
        return dataModels.first().round
    }

    companion object {
        fun newInstance(dataModels: MutableList<TournamentDataModel>, presenter: TournamentContract.Presenter): RoundFragment {
            return RoundFragment().apply {
                this.dataModels = dataModels
                this.presenter = presenter
            }
        }
    }
}