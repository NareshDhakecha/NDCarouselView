package com.ndsoftwares.carouselview

import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator

abstract class SmartTabIndicationInterpolator {
    abstract fun getLeftEdge(offset: Float): Float
    abstract fun getRightEdge(offset: Float): Float
    open fun getThickness(offset: Float): Float {
        return 1f //Always the same thickness by default
    }

    class SmartIndicationInterpolator @JvmOverloads constructor(factor: Float = DEFAULT_INDICATOR_INTERPOLATION_FACTOR) : SmartTabIndicationInterpolator() {
        private val leftEdgeInterpolator: Interpolator
        private val rightEdgeInterpolator: Interpolator
        override fun getLeftEdge(offset: Float): Float {
            return leftEdgeInterpolator.getInterpolation(offset)
        }

        override fun getRightEdge(offset: Float): Float {
            return rightEdgeInterpolator.getInterpolation(offset)
        }

        override fun getThickness(offset: Float): Float {
            return 1f / (1.0f - getLeftEdge(offset) + getRightEdge(offset))
        }

        companion object {
            private const val DEFAULT_INDICATOR_INTERPOLATION_FACTOR = 3.0f
        }

        init {
            leftEdgeInterpolator = AccelerateInterpolator(factor)
            rightEdgeInterpolator = DecelerateInterpolator(factor)
        }
    }

    class LinearIndicationInterpolator : SmartTabIndicationInterpolator() {
        override fun getLeftEdge(offset: Float): Float {
            return offset
        }

        override fun getRightEdge(offset: Float): Float {
            return offset
        }
    }

    companion object {
        val SMART: SmartTabIndicationInterpolator = SmartIndicationInterpolator()
        val LINEAR: SmartTabIndicationInterpolator = LinearIndicationInterpolator()
        const val ID_SMART = 0
        const val ID_LINEAR = 1
        fun of(id: Int): SmartTabIndicationInterpolator {
            return when (id) {
                ID_SMART -> SMART
                ID_LINEAR -> LINEAR
                else -> throw IllegalArgumentException("Unknown id: $id")
            }
        }
    }
}