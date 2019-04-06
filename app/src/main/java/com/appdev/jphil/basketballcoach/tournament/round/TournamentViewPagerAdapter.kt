package com.appdev.jphil.basketballcoach.tournament.round

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.appdev.jphil.basketball.datamodels.TournamentDataModel
import com.appdev.jphil.basketballcoach.tournament.TournamentContract

class TournamentViewPagerAdapter(
    private val dataModels: MutableList<TournamentDataModel>,
    private val presenter: TournamentContract.Presenter,
    fm: FragmentManager?
) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return RoundFragment.newInstance(
            mutableListOf<TournamentDataModel>().apply{ addAll(dataModels.filter { it.round == position + 1 }) },
            presenter
        )
    }

    override fun getCount(): Int = if (dataModels.isNotEmpty()) dataModels.last().round else 0

    fun updateDataModels(newDataModels: MutableList<TournamentDataModel>) {
        dataModels.clear()
        dataModels.addAll(newDataModels)
    }
}