package com.appdev.jphil.basketballcoach.recruitoverview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(RECRUIT_ID, recruitId)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recruit_overview, container, false)
    }

    override fun displayRecruit(recruit: Recruit) {
        view?.findViewById<TextView>(R.id.name)?.text = recruit.fullName
        view?.findViewById<RecyclerView>(R.id.recycler_view)?.let {
            it.adapter = RecruitOverviewAdapter(recruit.interestInTeams, resources)
            it.layoutManager = LinearLayoutManager(context)
        }
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