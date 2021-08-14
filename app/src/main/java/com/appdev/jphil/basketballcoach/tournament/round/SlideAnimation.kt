package com.appdev.jphil.basketballcoach.tournament.round

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation

class SlideAnimation(
    private val startHeight: Int,
    private val endHeight: Int,
    private val view: View
) : Animation() {

    override fun willChangeBounds(): Boolean = true

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        if (view.height != endHeight) {
            val newHeight = (startHeight + ((endHeight - startHeight) * interpolatedTime))
            view.layoutParams.height = newHeight.toInt()
            view.requestLayout()
        }
    }
}
