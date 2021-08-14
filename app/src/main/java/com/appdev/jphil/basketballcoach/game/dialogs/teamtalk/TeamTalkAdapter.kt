package com.appdev.jphil.basketballcoach.game.dialogs.teamtalk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appdev.jphil.basketball.game.CoachTalk
import com.appdev.jphil.basketballcoach.databinding.ListItemTeamTalkBinding

class TeamTalkAdapter(
    private val talks: List<TeamTalkDataModel>,
    private val onClick: (type: CoachTalk) -> Unit
) : RecyclerView.Adapter<TeamTalkAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListItemTeamTalkBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ListItemTeamTalkBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = talks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = talks[position]
        holder.binding.apply {
            text.text = data.text
            root.setOnClickListener { onClick(data.type) }
        }
    }
}
