package com.appdev.jphil.basketballcoach.recruitoverview

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.main.*
import com.appdev.jphil.basketballcoach.main.injection.qualifiers.TeamId
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RecruitOverviewFragment : Fragment(), RecruitOverviewContract.View {

    @Inject
    lateinit var presenter: RecruitOverviewContract.Presenter
    @Inject
    lateinit var factory: ViewModelFactory
    private var teamManager: TeamManagerViewModel? = null

    var recruitId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (recruitId == 0) {
            recruitId = savedInstanceState?.getInt(RECRUIT_ID, 0) ?: 0
        }
        AndroidSupportInjection.inject(this)
        teamManager = (activity as? MainActivity)?.getTeamViewModel(factory)
        presenter.onViewAttached(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.fetchData()
    }

    override fun onStop() {
        presenter.onViewDetached()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(RECRUIT_ID, recruitId)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as? NavigationManager)?.setToolbarTitle(resources.getString(R.string.recruit_overview))
        return inflater.inflate(R.layout.fragment_recruit_overview, container, false).apply {
            findViewById<Button>(R.id.button_scout).setOnClickListener { presenter.onScoutClicked() }
            findViewById<Button>(R.id.button_scholarship).setOnClickListener { presenter.onOfferScholarshipClicked() }
        }
    }

    override fun displayRecruit(recruit: Recruit) {
        view?.apply {
            findViewById<TextView>(R.id.position).text = resources.getStringArray(R.array.position_abbreviation)[recruit.position - 1]
            findViewById<TextView>(R.id.player_name).text = recruit.fullName

            val teamId = teamManager?.teamId ?: -1
            val ratingMin = recruit.getRatingMinForTeam(teamId)
            val ratingMax = recruit.getRatingMaxForTeam(teamId)
            findViewById<TextView>(R.id.rating).text = if (ratingMax != ratingMin) {
                resources.getString(R.string.rating_range, ratingMin, ratingMax)
            } else {
                resources.getString(R.string.rating_colon, ratingMax)
            }

            findViewById<TextView>(R.id.potential).text = resources.getString(R.string.potential_color, recruit.potential)
            findViewById<TextView>(R.id.type).text = resources.getStringArray(R.array.player_types)[recruit.playerType.type]

            findViewById<RecyclerView>(R.id.recycler_view)?.let {
                it.adapter = RecruitOverviewAdapter(recruit, resources)
                it.layoutManager = LinearLayoutManager(context)
            }
        }
    }

    override fun disableAllButtons() {
        view?.apply {
            findViewById<Button>(R.id.button_scout).isEnabled = false
            findViewById<Button>(R.id.button_scholarship).isEnabled = false
        }
    }

    override fun showToast(message: String) {
        Toast.makeText(context!!, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val RECRUIT_ID = "id"
        private const val TEAM_ID = "tId"

        fun newInstance(recruitId: Int): RecruitOverviewFragment {
            val fragment = RecruitOverviewFragment()
            fragment.recruitId = recruitId
            return fragment
        }
    }
}