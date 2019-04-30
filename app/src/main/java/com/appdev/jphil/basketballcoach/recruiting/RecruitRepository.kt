package com.appdev.jphil.basketballcoach.recruiting

import com.appdev.jphil.basketballcoach.database.BasketballDatabase
import com.appdev.jphil.basketballcoach.database.recruit.RecruitDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecruitRepository @Inject constructor(
    private val database: BasketballDatabase
) : RecruitContract.Repository {

    private lateinit var presenter: RecruitContract.Presenter

    override fun loadRecruits() {
        GlobalScope.launch(Dispatchers.IO) {
            val recruits = RecruitDatabaseHelper.loadAllRecruits(database)
            withContext(Dispatchers.Main) {
                presenter.onRecruitsLoaded(recruits)
            }
        }
    }

    override fun attachPresenter(presenter: RecruitContract.Presenter) {
        this.presenter = presenter
    }
}