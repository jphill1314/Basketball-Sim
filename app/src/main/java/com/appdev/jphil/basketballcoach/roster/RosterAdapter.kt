package com.appdev.jphil.basketballcoach.roster

import android.content.res.Resources
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appdev.jphil.basketballcoach.R
import com.appdev.jphil.basketballcoach.databinding.ListItemHeaderBinding
import com.appdev.jphil.basketballcoach.databinding.ListItemRosterBinding

class RosterAdapter(
    val presenter: RosterContract.Presenter,
    val roster: MutableList<RosterDataModel>,
    val resources: Resources
) : RecyclerView.Adapter<RosterAdapter.ViewHolder>() {

    private val positions: Array<String> = resources.getStringArray(R.array.position_abbreviation)
    private val classes: Array<String> = resources.getStringArray(R.array.years)
    var isUsersTeam = false

    abstract class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    class PlayerViewHolder(val binding: ListItemRosterBinding): ViewHolder(binding.root)
    class HeaderViewHolder(val binding: ListItemHeaderBinding): ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (type == 0) {
            HeaderViewHolder(ListItemHeaderBinding.inflate(inflater, parent, false))
        } else {
            PlayerViewHolder(ListItemRosterBinding.inflate(inflater, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> 0
            7 -> 0
            else -> 1
        }
    }

    override fun getItemCount(): Int {
        return if (roster.isNotEmpty()) roster.size + 4 else 0
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        when (viewHolder) {
            is HeaderViewHolder -> bindHeader(viewHolder.binding, position)
            is PlayerViewHolder -> bindPlayer(viewHolder.binding, position)
        }
    }

    private fun bindHeader(binding: ListItemHeaderBinding, position: Int) {
        if (position == 0) {
            binding.title.text = resources.getString(R.string.starting_lineup)
        } else {
            binding.title.text = resources.getString(R.string.bench)
        }
    }

    private fun bindPlayer(binding: ListItemRosterBinding, position: Int) {
        if (position == 1 || position == 8) {
            binding.position.text = resources.getString(R.string.pos)
            binding.name.text = resources.getString(R.string.name)
            binding.rating.text = resources.getString(R.string.rating)
            binding.year.text = resources.getString(R.string.year)

            val params = binding.root.layoutParams as ViewGroup.MarginLayoutParams
            params.bottomMargin = resources.getDimensionPixelSize(R.dimen.card_margin_bottom)
            setTextColor(binding, false)
        } else {
            val dataModel = if (position < 7) roster[position - 2] else roster[position - 4]
            val player = dataModel.player
            setTextColor(binding, dataModel.isSelected)

            binding.position.text = if (position < 7) positions[position - 2] else positions[player.position - 1]
            binding.name.text = player.fullName
            binding.rating.text = player.getOverallRating().toString()
            binding.year.text = classes[player.year]

            if (position == 6) {
                val params = binding.root.layoutParams as ViewGroup.MarginLayoutParams
                params.bottomMargin = resources.getDimensionPixelSize(R.dimen.sixteen_dp)
            }

            if (isUsersTeam) {
                binding.root.setOnClickListener {
                    presenter.onPlayerSelected(player)
                    dataModel.isSelected = !dataModel.isSelected
                    notifyDataSetChanged()
                }
            }
            binding.root.setOnLongClickListener {
                presenter.onPlayerLongPressed(player)
                true
            }
        }
    }

    private fun setTextColor(binding: ListItemRosterBinding, isSelected: Boolean) {
        val color = if (isSelected) R.color.gray else R.color.black
        binding.position.setTextColor(ResourcesCompat.getColor(resources, color, null))
        binding.name.setTextColor(ResourcesCompat.getColor(resources, color, null))
        binding.rating.setTextColor(ResourcesCompat.getColor(resources, color, null))
        binding.year.setTextColor(ResourcesCompat.getColor(resources, color, null))
    }

    fun updateRoster(roster: MutableList<RosterDataModel>) {
        // TODO: diff util?
        this.roster.clear()
        this.roster.addAll(roster)
        notifyDataSetChanged()
    }
}