package com.nasahapps.sampleflickrapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp

@Composable
fun PaddingValues.copy(
    start: Dp? = null,
    top: Dp? = null,
    end: Dp? = null,
    bottom: Dp? = null
): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    return PaddingValues(
        start ?: this.calculateStartPadding(layoutDirection),
        top ?: this.calculateTopPadding(),
        end ?: this.calculateEndPadding(layoutDirection),
        bottom ?: this.calculateBottomPadding()
    )
}