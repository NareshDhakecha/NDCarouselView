package com.ndsoftwares.carouselview.transform

import android.animation.ArgbEvaluator
import android.graphics.PorterDuff
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import kotlin.math.abs

class ScaleTransformer : CVScrollItemTransformer {
    private var pivotX: Pivot = Pivot.X.CENTER.create()
    private var pivotY: Pivot = Pivot.Y.CENTER.create()
    private var minScale: Float = 0.6f
    private var maxMinDiff: Float = 0.4f
    private val colorEvaluator = ArgbEvaluator()



    override fun transformItem(item: View, position: Float, @ColorInt color: Int) {
        pivotX.setOn(item)
        pivotY.setOn(item)
        val closenessToCenter = 1f - abs(position)
        val scale = minScale + maxMinDiff * closenessToCenter
        item.scaleX = scale
        item.scaleY = scale
//        item.background.setColorFilter(calculateBgColor(color, closenessToCenter), PorterDuff.Mode.SRC_ATOP)
        item.background.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                calculateBgColor(color, closenessToCenter),
                BlendModeCompat.SRC_ATOP
            )
    }

    @ColorInt
    private fun calculateBgColor(color: Int, scale: Float): Int = colorEvaluator.evaluate(
        scale,
        android.R.color.transparent,
        color
    ) as Int

    class Builder {
        private val transformer: ScaleTransformer = ScaleTransformer()
        private var maxScale: Float
        fun setMinScale(@FloatRange(from = 0.01) scale: Float): Builder {
            transformer.minScale = scale
            return this
        }

        fun setMaxScale(@FloatRange(from = 0.01) scale: Float): Builder {
            maxScale = scale
            return this
        }

        fun setPivotX(pivotX: Pivot.X): Builder {
            return setPivotX(pivotX.create())
        }

        fun setPivotX(pivot: Pivot): Builder {
            assertAxis(pivot, Pivot.AXIS_X)
            transformer.pivotX = pivot
            return this
        }

        fun setPivotY(pivotY: Pivot.Y): Builder {
            return setPivotY(pivotY.create())
        }

        fun setPivotY(pivot: Pivot): Builder {
            assertAxis(pivot, Pivot.AXIS_Y)
            transformer.pivotY = pivot
            return this
        }

        fun build(): ScaleTransformer {
            transformer.maxMinDiff = maxScale - transformer.minScale
            return transformer
        }

        private fun assertAxis(pivot: Pivot, @Pivot.Axis axis: Int) {
            require(pivot.axis == axis) { "You passed a Pivot for wrong axis." }
        }

        init {
            maxScale = 1f
        }
    }

}