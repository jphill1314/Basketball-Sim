package com.appdev.jphil.basketballcoach.recruiting

import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appdev.jphil.basketball.recruits.Recruit
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_recruit, parent, false) // TODO: make own layout
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return recruits.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val recruit = recruits[position]
        viewHolder.position.text = positionNames[recruit.position - 1]
        viewHolder.name.text = recruit.fullName
        viewHolder.rating.text = resources.getString(R.string.rating_colon, recruit.rating)
        viewHolder.playerType.text = playerTypes[recruit.playerType.type]

        val interestInTeam = recruit.interestInTeams.filter { it.teamId == teamId }
        if (interestInTeam.isNotEmpty()) {
            val interest = interestInTeam.first()
            viewHolder.interest.text = resources.getString(R.string.interest_colon, interest.interest.toString())
            val interaction = when {
                interest.isOfficialVisitDone -> 3
                interest.isOfferedScholarship -> 2
                interest.isContacted -> 1
                interest.isScouted -> 0
                else -> -1
            }
            viewHolder.interaction.text = if (interaction == -1) "" else interactions[interaction]
        } else {
            viewHolder.interest.text = resources.getString(R.string.interest_colon, resources.getString(R.string.unknown))
        }

        if (recruit.isCommitted) {
            viewHolder.interaction.text = if (recruit.teamCommittedTo == teamId) "Committed to you" else "Committed elsewhere"
        }

        viewHolder.itemView.setOnLongClickListener {
            presenter.onRecruitLongPressed(recruit)
            true
        }
    }
}