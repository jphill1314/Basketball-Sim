package com.appdev.jphil.basketballcoach.tournament

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.game.GameFragment
import com.appdev.jphil.basketballcoach.main.NavigationManager
import com.appdev.jphil.basketball.datamodels.TournamentDataModel
import com.appdev.jphil.basketballcoach.tournament.round.RoundFragment
import com.appdev.jphil.basketballcoach.tournament.round.TournamentViewPagerAdapter
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class TournamentFragment : Fragment(), TournamentContract.View, ViewPager.OnPageChangeListener {

    @Inject
    lateinit var presenter: TournamentContract.Presenter

    private lateinit var fab: FloatingActionButton
    private var adapter: TournamentViewPagerAdapter? = null
    var confId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? NavigationManager)?.let {
            confId = it.getConferenceId()
        }
        AndroidSupportInjection.inject(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewAttached(this)
    }

    override fun onStop() {
        presenter.onViewDetached()
        super.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tournament, container, false)
        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener { presenter.onFABClicked() }
        fab.show()

        return view
    }

    override fun onTournamentLoaded(dataModels: MutableList<TournamentDataModel>) {
        if (adapter == null) {
            adapter = TournamentViewPagerAdapter(dataModels, presenter, childFragmentManager)
            view?.findViewById<ViewPager>(R.id.view_pager)?.let {
                it.adapter = adapter
                it.pageMargin = -(getScreenWidth() / 2)
                it.offscreenPageLimit = 10
                it.addOnPageChangeListener(this)
            }
        } else {
            adapter?.updateDataModels(dataModels)
            childFragmentManager.fragments.forEach { fragment ->
                if (fragment is RoundFragment) {
                    updateFragment(fragment, dataModels)
                }
            }
        }
    }

    override fun startGameFragment(gameId: Int, homeName: String, awayName: String, userIsHomeTeam: Boolean) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.frame_layout, GameFragment.newInstance(gameId, homeName, awayName, userIsHomeTeam))
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun getScreenWidth(): Int {
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
        return metrics.widthPixels
    }

    override fun onPageScrollStateChanged(p0: Int) {

    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

    }

    override fun onPageSelected(position: Int) {
        childFragmentManager.fragments.forEach {
            (it as? RoundFragment)?.updateRound(position + 1)
        }
    }

    private fun updateFragment(fragment: RoundFragment, dataModels: MutableList<TournamentDataModel>) {
        fragment.addData(mutableListOf<TournamentDataModel>().apply {
            addAll(dataModels.filter { it.round == fragment.getFragmentRound() })
        })
    }

    companion object {
        fun newInstance(): TournamentFragment {
            return TournamentFragment()
        }
    }
}