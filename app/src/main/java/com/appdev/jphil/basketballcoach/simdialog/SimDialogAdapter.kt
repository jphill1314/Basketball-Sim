package com.appdev.jphil.basketballcoach.simdialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appdev.jphil.basketballcoach.databinding.ListItemSimpleGameResultBinding

class SimDialogAdapter : RecyclerView.Adapter<SimDialogAdapter.ViewHolder>() {

    private val items = mutableListOf<SimDialogDataModel>()

    fun updateItems(newItems: List<SimDialogDataModel>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ListItemSimpleGameResultBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
        holder.binding.apply {
            homeName.text = data.homeName
            awayName.text = data.awayName
            homeScore.text = data.homeScore.toString()
            awayScore.text = data.awayScore.toString()
        }
    }

    class ViewHolder(val binding: ListItemSimpleGameResultBinding) : RecyclerView.ViewHolder(binding.root)
}
