package com.appdev.jphil.basketballcoach.util

import android.content.res.Resources
import androidx.core.content.res.ResourcesCompat
import com.appdev.jphil.basketball.teams.TeamColor
import com.appdev.jphil.basketballcoach.R

fun TeamColor.getStyle() = when (this) {
    TeamColor.Red -> R.style.AppTheme_Red
    TeamColor.Pink -> R.style.AppTheme_Pink
    TeamColor.Purple -> R.style.AppTheme_Purple
    TeamColor.DeepPurple -> R.style.AppTheme_DeepPurple
    TeamColor.Indigo -> R.style.AppTheme_Indigo
    TeamColor.Blue -> R.style.AppTheme_Blue
    TeamColor.LightBlue -> R.style.AppTheme_LightBlue
    TeamColor.Cyan -> R.style.AppTheme_Cyan
    TeamColor.Teal -> R.style.AppTheme_Teal
    TeamColor.Green -> R.style.AppTheme_Green
    TeamColor.LightGreen -> R.style.AppTheme_LightGreen
    TeamColor.Yellow -> R.style.AppTheme_Yellow
    TeamColor.Orange -> R.style.AppTheme_Orange
    TeamColor.DeepOrange -> R.style.AppTheme_DeepOrange
    TeamColor.BlueGrey -> R.style.AppTheme_BlueGrey
}

fun TeamColor.getColor() = when (this) {
    TeamColor.Red -> R.color.red
    TeamColor.Pink -> R.color.pink
    TeamColor.Purple -> R.color.purple
    TeamColor.DeepPurple -> R.color.deepPurple
    TeamColor.Indigo -> R.color.indigo
    TeamColor.Blue -> R.color.blue
    TeamColor.LightBlue -> R.color.lightBlue
    TeamColor.Cyan -> R.color.cyan
    TeamColor.Teal -> R.color.teal
    TeamColor.Green -> R.color.green
    TeamColor.LightGreen -> R.color.lightGreen
    TeamColor.Yellow -> R.color.yellow
    TeamColor.Orange -> R.color.orange
    TeamColor.DeepOrange -> R.color.deepOrange
    TeamColor.BlueGrey -> R.color.blueGrey
}

fun Resources.getColorCompat(color: Int) = ResourcesCompat.getColor(this, color, null)
