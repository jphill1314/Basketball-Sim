package com.appdev.jphil.basketballcoach.recruitoverview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketballcoach.R
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RecruitOverviewFragment : Fragment(), RecruitOverviewContract.View {

    @Inject
    lateinit var presenter: RecruitOverviewContract.Presenter
    var recruitId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (recruitId == 0) {
            recruitId = savedInstanceState?.getInt(RECRUIT_ID, 0) ?: 0
        }
        AndroidSupportInjection.inject(this)
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
        return inflater.inflate(R.layout.fragment_recruit_overview, container, false).apply {
            findViewById<Button>(R.id.button_scout).setOnClickListener { presenter.onScoutClicked() }
            findViewById<Button>(R.id.button_scholarship).setOnClickListener { presenter.onOfferScholarshipClicked() }
        }
    }

    override fun displayRecruit(recruit: Recruit) {
        view?.apply {
            findViewById<TextView>(R.id.position).text = resources.getStringArray(R.array.position_abbreviation)[recruit.position - 1]
            findViewById<TextView>(R.id.player_name).text = recruit.fullName
            findViewById<TextView>(R.id.rating).text = resources.getString(R.string.rating_colon, recruit.rating)
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

        fun newInstance(recruitId: Int): RecruitOverviewFragment {
            val fragment = RecruitOverviewFragment()
            fragment.recruitId = recruitId
            return fragment
        }
    }
}