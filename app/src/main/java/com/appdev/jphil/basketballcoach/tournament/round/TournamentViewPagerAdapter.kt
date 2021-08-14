package com.appdev.jphil.basketballcoach.tournament.round

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.appdev.jphil.basketball.datamodels.TournamentDataModel
import com.appdev.jphil.basketballcoach.tournament.TournamentContract

class TournamentViewPagerAdapter(
    private val dataModels: MutableList<TournamentDataModel>,
    private val presenter: TournamentContract.Presenter,
    fm: FragmentManager
) : FragmentStatePagerAdapter(fm) {

    // TODO: replace with resources
    private val titles = listOf("Round 1", "Round 2", "Semi-finals", "Championship")

    override fun getItem(position: Int): Fragment {
        return RoundFragment.newInstance(
            mutableListOf<TournamentDataModel>().apply { addAll(dataModels.filter { it.round == position + 1 }) },
            presenter
        )
    }

    override fun getCount(): Int = if (dataModels.isNotEmpty()) dataModels.last().round else 0

    override fun getPageTitle(position: Int) = titles[position]

    fun updateDataModels(newDataModels: MutableList<TournamentDataModel>) {
        dataModels.clear()
        dataModels.addAll(newDataModels)
    }
}
