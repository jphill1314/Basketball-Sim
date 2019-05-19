package com.appdev.jphil.basketballcoach.recruitoverview

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketballcoach.R

class RecruitOverviewAdapter(
    private val recruit: Recruit,
    private val resources: Resources
) : RecyclerView.Adapter<RecruitOverviewAdapter.ViewHolder>() {

    private val interactions = resources.getStringArray(R.array.interactions)

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val team: TextView = view.findViewById(R.id.team_name)
        val interaction: TextView = view.findViewById(R.id.interaction)
        val interest: TextView = view.findViewById(R.id.interest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_recruit_interest, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = recruit.interestInTeams.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val teamInterest = recruit.interestInTeams[position]

        with (viewHolder) {
            team.text = teamInterest.teamName

            val index = when {
                teamInterest.isScholarshipRevoked -> 4
                teamInterest.isOfferedScholarship -> 2
                else -> -1
            }

            interaction.text = if (index == -1) "" else interactions[index]

            if (recruit.isCommitted) {
                if (recruit.teamCommittedTo == teamInterest.teamId) {
                    interaction.text = resources.getString(R.string.committed)
                }
            }

            interest.text = resources.getString(R.string.interest_colon, teamInterest.interest.toString())
        }
    }
}