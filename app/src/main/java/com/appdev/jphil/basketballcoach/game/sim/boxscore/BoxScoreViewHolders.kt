package com.appdev.jphil.basketballcoach.game.sim.boxscore

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.appdev.jphil.basketballcoach.databinding.ListItemBoxScoreBinding
import com.appdev.jphil.basketballcoach.databinding.ListItemBoxScoreHeaderBinding
import com.appdev.jphil.basketballcoach.databinding.ListItemBoxScoreLeftBinding

abstract class BoxScoreViewHolder(view: View) : RecyclerView.ViewHolder(view)

class HeaderViewHolder(val binding: ListItemBoxScoreHeaderBinding): BoxScoreViewHolder(binding.root)

class BoxScorePlayerViewHolder(val binding: ListItemBoxScoreBinding): BoxScoreViewHolder(binding.root)

class BoxScoreLeftViewHolder(val binding: ListItemBoxScoreLeftBinding): BoxScoreViewHolder(binding.root)
