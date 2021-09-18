package com.appdev.jphil.basketballcoach.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val orange = Color(0xfffa8320)
val orangeLight = Color(0xffffb453)
val orangeDark = Color(0xffc15400)

val rust = Color(0xffb54213)
val rustLight = Color(0xffed713f)
val rustDark = Color(0xff7f0d00)

val lightColors = lightColors(
    primary = orange,
    primaryVariant = orangeDark,
    secondary = rust,
    secondaryVariant = rustDark,
    onPrimary = Color.Black,
    onSecondary = Color.White
)

val darkColors = darkColors(
    primary = orange,
    primaryVariant = orangeDark,
    secondary = rust,
    secondaryVariant = rustDark,
    onPrimary = Color.Black,
    onSecondary = Color.White
)
