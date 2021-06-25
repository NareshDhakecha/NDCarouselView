package com.ndsoftwares.carouselview.transform

import android.view.View
import androidx.annotation.ColorInt

interface CVScrollItemTransformer {
//    fun transformItem(item: View, position: Float)
    fun transformItem(item: View, position: Float, @ColorInt color: Int)
}