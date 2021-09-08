package com.appdev.jphil.basketballcoach.recruitoverview

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketballcoach.R

class RecruitOverviewAdapter(
    private val recruit: Recruit,
    private val resources: Resources
) : RecyclerView.Adapter<RecruitOverviewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val team: TextView = view.findViewById(R.id.team_name)
        val interaction: TextView = view.findViewById(R.id.interaction)
        val interest: TextView = view.findViewById(R.id.interest)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_recruit_interest, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = recruit.recruitInterests.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val teamInterest = recruit.recruitInterests[position]

        with(viewHolder) {
//            team.text = teamInterest.teamName

            if (recruit.isCommitted) {
                if (recruit.teamCommittedTo == teamInterest.teamId) {
                    interaction.text = resources.getString(R.string.committed)
                }
            }

            interest.text = resources.getString(R.string.interest_colon, teamInterest.getInterest().toString())
        }
    }
}
