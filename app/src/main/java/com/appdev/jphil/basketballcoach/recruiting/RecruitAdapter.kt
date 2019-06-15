package com.appdev.jphil.basketballcoach.recruiting

import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.recruits.Recruit
import com.appdev.jphil.basketball.teams.Team
import com.appdev.jphil.basketballcoach.R

class RecruitAdapter(
    private var recruits: List<Recruit>,
    private val teamId: Int,
    private val presenter: RecruitContract.Presenter,
    private val resources: Resources
): RecyclerView.Adapter<RecruitAdapter.ViewHolder>() {

    private val positionNames = resources.getStringArray(R.array.position_abbreviation)
    private val playerTypes = resources.getStringArray(R.array.player_types)
    private val interactions = resources.getStringArray(R.array.interactions)

    fun updateRecruits(newRecruits: List<Recruit>) {
        recruits = newRecruits
        notifyDataSetChanged()
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val position: TextView = view.findViewById(R.id.position)
        val name: TextView = view.findViewById(R.id.name)
        val rating: TextView = view.findViewById(R.id.rating)
        val playerType: TextView = view.findViewById(R.id.player_type)
        val interest: TextView = view.findViewById(R.id.interest)
        val interaction: TextView = view.findViewById(R.id.interation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_recruit, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (recruits.isNotEmpty()) recruits.size else 1
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if (recruits.isEmpty()) {
            viewHolder.name.text = resources.getString(R.string.no_known_recruits)
        } else {
            val recruit = recruits[position]
            viewHolder.position.text = positionNames[recruit.position - 1]
            viewHolder.name.text = recruit.fullName
            viewHolder.playerType.text = playerTypes[recruit.playerType.type]
            // TODO: make sure to only show the range that the user knows about
            val ratingMin = recruit.getRatingMinForTeam(teamId)
            val ratingMax = recruit.getRatingMaxForTeam(teamId)
            viewHolder.rating.text = if (ratingMax != ratingMin) {
                resources.getString(R.string.rating_range, ratingMin, ratingMax)
            } else {
                resources.getString(R.string.rating_colon, ratingMax)
            }

            val interestInTeam = recruit.interestInTeams.filter { it.teamId == teamId }
            if (interestInTeam.isNotEmpty()) {
                val interest = interestInTeam.first()
                viewHolder.interest.text = resources.getString(R.string.interest_colon, interest.interest.toString())
                val interaction = when {
                    interest.isScholarshipRevoked -> 4
                    interest.isOfferedScholarship -> 2
                    else -> -1
                }
                if (recruit.isCommitted) {
                    viewHolder.interaction.text =
                        if (recruit.teamCommittedTo == teamId) "Committed to you" else "Committed elsewhere"
                } else {
                    viewHolder.interaction.text = if (interaction == -1) "" else interactions[interaction]
                }
            } else {
                viewHolder.interest.text =
                    resources.getString(R.string.interest_colon, resources.getString(R.string.unknown))
                viewHolder.interaction.text = ""
            }
            viewHolder.itemView.setOnClickListener { presenter.onRecruitPressed(recruit) }
        }
    }
}