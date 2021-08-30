package com.appdev.jphil.basketballcoach.tournament.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout

@Composable
fun TenTeamTournamentView(
    modifier: Modifier = Modifier,
    contents: @Composable () -> Unit
) {
    Layout(
        modifier = modifier
            .background(Color.LightGray)
            .horizontalScroll(rememberScrollState())
            .verticalScroll(rememberScrollState()),
        content = contents
    ) { measurables, constraints ->
        var maxHeight = 0
        var maxWidth = 0
        var totalWidth = 0
        var totalHeight = 0

        val itemConstraints = constraints.copy(
            maxWidth = (constraints.minWidth * 0.8).toInt(),
            minWidth = 0,
            minHeight = 0
        )

        val placeables = measurables.map {
            it.measure(itemConstraints).also { placeable ->
                if (placeable.height > maxHeight) {
                    maxHeight = placeable.height
                }
                if (placeable.width > maxWidth) {
                    maxWidth = placeable.width
                }
                totalWidth += placeable.width
                totalHeight += placeable.height
            }
        }

        layout(totalWidth, totalHeight) {
            placeables.forEachIndexed { index, placeable ->
                // TODO: prevent user from scrolling everything off screen
                // TODO: animate item size?
                when (index) {
                    // Play in round
                    0 -> placeable.placeRelative(x = 0, y = 0)
                    1 -> placeable.placeRelative(x = 0, y = maxHeight * 3)
                    // 1st round
                    2 -> placeable.placeRelative(x = maxWidth, y = 0)
                    3 -> placeable.placeRelative(x = maxWidth, y = maxHeight)
                    4 -> placeable.placeRelative(x = maxWidth, y = maxHeight * 2)
                    5 -> placeable.placeRelative(x = maxWidth, y = maxHeight * 3)
                    // 2nd round
                    6 -> placeable.placeRelative(x = 2 * maxWidth, y = (maxHeight * 0.5).toInt())
                    7 -> placeable.placeRelative(x = 2 * maxWidth, y = (maxHeight * 2.5).toInt())
                    // 3rd round
                    8 -> placeable.placeRelative(x = 3 * maxWidth, y = (maxHeight * 1.5).toInt())
                }
            }
        }
    }
}

@Composable
fun EightTeamTournamentView(
    modifier: Modifier = Modifier,
    contents: @Composable () -> Unit
) {
    Layout(
        modifier = modifier
            .background(Color.LightGray)
            .horizontalScroll(rememberScrollState())
            .verticalScroll(rememberScrollState()),
        content = contents
    ) { measurables, constraints ->
        var maxHeight = 0
        var maxWidth = 0
        var totalWidth = 0
        var totalHeight = 0

        val itemConstraints = constraints.copy(
            maxWidth = (constraints.minWidth * 0.8).toInt(),
            minWidth = 0,
            minHeight = 0
        )

        val placeables = measurables.map {
            it.measure(itemConstraints).also { placeable ->
                if (placeable.height > maxHeight) {
                    maxHeight = placeable.height
                }
                if (placeable.width > maxWidth) {
                    maxWidth = placeable.width
                }
                totalWidth += placeable.width
                totalHeight += placeable.height
            }
        }

        layout(totalWidth, totalHeight) {
            placeables.forEachIndexed { index, placeable ->
                // TODO: prevent user from scrolling everything off screen
                // TODO: animate item size?
                when (index) {
                    // 1st round
                    0 -> placeable.placeRelative(x = 0, y = 0)
                    1 -> placeable.placeRelative(x = 0, y = maxHeight)
                    2 -> placeable.placeRelative(x = 0, y = maxHeight * 2)
                    3 -> placeable.placeRelative(x = 0, y = maxHeight * 3)
                    // 2nd round
                    4 -> placeable.placeRelative(x = maxWidth, y = (maxHeight * 0.5).toInt())
                    5 -> placeable.placeRelative(x = maxWidth, y = (maxHeight * 2.5).toInt())
                    // 3rd round
                    6 -> placeable.placeRelative(x = 2 * maxWidth, y = (maxHeight * 1.5).toInt())
                }
            }
        }
    }
}

@Composable
fun NationalChampionshipView(
    modifier: Modifier = Modifier,
    contents: @Composable () -> Unit
) {
    Layout(
        modifier = modifier
            .background(Color.LightGray)
            .horizontalScroll(rememberScrollState())
            .verticalScroll(rememberScrollState()),
        content = contents
    ) { measurables, constraints ->
        var maxHeight = 0
        var maxWidth = 0
        var totalWidth = 0
        var totalHeight = 0

        val itemConstraints = constraints.copy(
            maxWidth = (constraints.minWidth * 0.8).toInt(),
            minWidth = 0,
            minHeight = 0
        )

        val placeables = measurables.map {
            it.measure(itemConstraints).also { placeable ->
                if (placeable.height > maxHeight) {
                    maxHeight = placeable.height
                }
                if (placeable.width > maxWidth) {
                    maxWidth = placeable.width
                }
                totalWidth += placeable.width
                totalHeight += placeable.height
            }
        }

        layout(totalWidth, totalHeight) {
            placeables.forEachIndexed { index, placeable ->
                // TODO: prevent user from scrolling everything off screen
                // TODO: animate item size? <- this is almost def gonna be needed here
                // TODO: consider some sort of tab switcher to break this apart in app
                when (index) {
                    // 1st round
                    0 -> placeable.placeRelative(x = 0, y = 0)
                    1 -> placeable.placeRelative(x = 0, y = maxHeight)

                    2 -> placeable.placeRelative(x = 0, y = maxHeight * 2)
                    3 -> placeable.placeRelative(x = 0, y = maxHeight * 3)

                    4 -> placeable.placeRelative(x = 0, y = maxHeight * 4)
                    5 -> placeable.placeRelative(x = 0, y = maxHeight * 5)

                    6 -> placeable.placeRelative(x = 0, y = maxHeight * 6)
                    7 -> placeable.placeRelative(x = 0, y = maxHeight * 7)

                    8 -> placeable.placeRelative(x = 0, y = maxHeight * 8)
                    9 -> placeable.placeRelative(x = 0, y = maxHeight * 9)

                    10 -> placeable.placeRelative(x = 0, y = maxHeight * 10)
                    11 -> placeable.placeRelative(x = 0, y = maxHeight * 11)

                    12 -> placeable.placeRelative(x = 0, y = maxHeight * 12)
                    13 -> placeable.placeRelative(x = 0, y = maxHeight * 13)

                    14 -> placeable.placeRelative(x = 0, y = maxHeight * 14)
                    15 -> placeable.placeRelative(x = 0, y = maxHeight * 15)
                    // 2nd Round
                    16 -> placeable.placeRelative(x = maxWidth, y = (maxHeight * 0.5).toInt())
                    17 -> placeable.placeRelative(x = maxWidth, y = (maxHeight * 2.5).toInt())

                    18 -> placeable.placeRelative(x = maxWidth, y = (maxHeight * 4.5).toInt())
                    19 -> placeable.placeRelative(x = maxWidth, y = (maxHeight * 6.5).toInt())

                    20 -> placeable.placeRelative(x = maxWidth, y = (maxHeight * 8.5).toInt())
                    21 -> placeable.placeRelative(x = maxWidth, y = (maxHeight * 10.5).toInt())

                    22 -> placeable.placeRelative(x = maxWidth, y = (maxHeight * 12.5).toInt())
                    23 -> placeable.placeRelative(x = maxWidth, y = (maxHeight * 14.5).toInt())
                    // 3rd Round
                    24 -> placeable.placeRelative(x = maxWidth * 2, y = (maxHeight * 1.5).toInt())
                    25 -> placeable.placeRelative(x = maxWidth * 2, y = (maxHeight * 5.5).toInt())

                    26 -> placeable.placeRelative(x = maxWidth * 2, y = (maxHeight * 9.5).toInt())
                    27 -> placeable.placeRelative(x = maxWidth * 2, y = (maxHeight * 13.5).toInt())
                    // 4th Round
                    28 -> placeable.placeRelative(x = maxWidth * 3, y = (maxHeight * 3.5).toInt())
                    29 -> placeable.placeRelative(x = maxWidth * 3, y = (maxHeight * 11.5).toInt())
                    // 5th Round
                    30 -> placeable.placeRelative(x = maxWidth * 4, y = (maxHeight * 7.5).toInt())
                }
            }
        }
    }
}
