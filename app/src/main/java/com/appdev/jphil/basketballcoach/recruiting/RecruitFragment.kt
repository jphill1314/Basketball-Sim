package com.appdev.jphil.basketballcoach.recruiting

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketballcoach.R
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RecruitFragment : Fragment(), RecruitContract.View {

    @Inject
    lateinit var presenter: RecruitContract.Presenter

    override fun onResume() {
        super.onResume()
        AndroidSupportInjection.inject(this)
        presenter.onViewAttached(this)
        presenter.fetchData()
    }

    override fun onStop() {
        presenter.onViewDetached()
        super.onStop()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_coaches, container, false) // TODO: make own layout
    }

    override fun displayRecruits(recruits: MutableList<Recruit>) {
        view?.findViewById<RecyclerView>(R.id.recycler_view)?.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = RecruitAdapter(recruits, resources)
        }
    }
}