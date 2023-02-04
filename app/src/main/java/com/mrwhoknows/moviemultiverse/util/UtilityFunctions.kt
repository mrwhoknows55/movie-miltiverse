package com.mrwhoknows.moviemultiverse.util

import android.view.View

fun View.startFadeIn(duration: Long = 1000) {
    animate().alpha(1f).setDuration(duration).setStartDelay(duration / 100).start()
}

fun View.startFadeOut(duration: Long = 1000) {
    animate().alpha(0f).setDuration(duration).setStartDelay(duration / 100).start()
}