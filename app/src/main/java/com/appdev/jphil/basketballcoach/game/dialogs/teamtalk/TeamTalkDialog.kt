package com.appdev.jphil.basketballcoach.game.dialogs.teamtalk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.appdev.jphil.basketball.coaches.Coach
import com.appdev.jphil.basketball.game.CoachTalk
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.DialogTeamTalkBinding

class TeamTalkDialog : DialogFragment() {

    var onSelectCallback: (() -> Unit)? = null
    var coach: Coach? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        val binding = DialogTeamTalkBinding.inflate(inflater, container, false)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TeamTalkAdapter(getTeamTalks()) {
                coach?.teamTalkType = it
                onSelectCallback?.invoke()
                dismiss()
            }
        }
        return binding.root
    }

    private fun getTeamTalks(): List<TeamTalkDataModel> {
        return listOf(
            TeamTalkDataModel(resources.getString(R.string.neutral_talk), CoachTalk.NEUTRAL),
            TeamTalkDataModel(resources.getString(R.string.calm_talk), CoachTalk.CALM),
            TeamTalkDataModel(resources.getString(R.string.aggressive_talk), CoachTalk.AGGRESSIVE),
            TeamTalkDataModel(resources.getString(R.string.offensive_talk), CoachTalk.OFFENSIVE),
            TeamTalkDataModel(resources.getString(R.string.defensive_talk), CoachTalk.DEFENSIVE)
        )
    }
}