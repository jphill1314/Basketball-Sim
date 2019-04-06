package com.appdev.jphil.basketballcoach.util

import android.content.res.Resources
import android.util.TypedValue

object Pixels {

    fun dpToPx(dp: Float, resources: Resources) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
}