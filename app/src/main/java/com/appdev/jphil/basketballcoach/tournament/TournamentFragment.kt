package com.appdev.jphil.basketballcoach.tournament

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.appdev.jphil.basketball.datamodels.TournamentDataModel
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.main.NavigationManager
import com.appdev.jphil.basketballcoach.simdialog.SimDialog
import com.appdev.jphil.basketballcoach.simdialog.SimDialogState
import com.appdev.jphil.basketballcoach.tournament.round.RoundFragment
import com.appdev.jphil.basketballcoach.tournament.round.TournamentViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class TournamentFragment : Fragment(), TournamentContract.View, ViewPager.OnPageChangeListener {

    @Inject
    lateinit var presenter: TournamentContract.Presenter
    @Inject
    lateinit var navManager: NavigationManager

    private var adapter: TournamentViewPagerAdapter? = null
    private var dialog: SimDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
        navManager.disableDrawer()
        val vm = ViewModelProviders.of(this).get(TournamentViewModel::class.java)
        vm.presenter?.let { presenter = it } ?: run { vm.presenter = presenter }
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewAttached(this)
    }

    override fun onStop() {
        presenter.onViewDetached()
        navManager.enableDrawer()
        super.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tournament, container, false)
        adapter = null
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
                view?.findViewById<TabLayout>(R.id.tab_layout)?.setupWithViewPager(it) // TODO: customize?
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
        findNavController().navigate(
            TournamentFragmentDirections.actionTournamentFragmentToGamePreviewFragment(
                gameId,
                homeName,
                awayName,
                userIsHomeTeam
            )
        )
    }

    override fun showDialog() {
        adapter?.notifyDataSetChanged()
        dialog?.dismiss()
        SimDialog().let {
            dialog = it
            it.onDialogDismissed = { presenter.onCancelSim() }
            it.isCancelable = false
            it.show(fragmentManager!!, "sim")
        }
    }

    override fun hideDialog() {
        adapter?.notifyDataSetChanged()
        dialog?.dismiss()
        dialog = null
    }

    override fun setDialogState(state: SimDialogState) {
        if (dialog == null) {
            showDialog()
        }
        dialog?.setState(state)
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
        fragment.addData(
            mutableListOf<TournamentDataModel>().apply {
                addAll(dataModels.filter { it.round == fragment.getFragmentRound() })
            }
        )
    }
}
