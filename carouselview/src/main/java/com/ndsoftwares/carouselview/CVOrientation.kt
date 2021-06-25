package com.ndsoftwares.carouselview

import android.graphics.Point
import android.view.View

enum class CVOrientation {

    HORIZONTAL{
        override fun createHelper(): Helper = HorizontalHelper()
    },

    VERTICAL{
        override fun createHelper(): Helper = VerticalHelper()
    };


    //Package private
    abstract fun createHelper(): Helper

    interface Helper {
        fun getViewEnd(recyclerWidth: Int, recyclerHeight: Int): Int
        fun getDistanceToChangeCurrent(childWidth: Int, childHeight: Int): Int
        fun setCurrentViewCenter(recyclerCenter: Point, scrolled: Int, outPoint: Point)
        fun shiftViewCenter(direction: Direction, shiftAmount: Int, outCenter: Point)
        fun getFlingVelocity(velocityX: Int, velocityY: Int): Int
        fun getPendingDx(pendingScroll: Int): Int
        fun getPendingDy(pendingScroll: Int): Int
        fun offsetChildren(amount: Int, lm: RecyclerViewProxy)
        fun getDistanceFromCenter(center: Point, viewCenterX: Float, viewCenterY: Float): Float
        fun isViewVisible(
            center: Point,
            halfWidth: Int,
            halfHeight: Int,
            endBound: Int,
            extraSpace: Int
        ): Boolean

        fun hasNewBecomeVisible(lm: CVLayoutManager): Boolean
        fun canScrollVertically(): Boolean
        fun canScrollHorizontally(): Boolean
    }

    class HorizontalHelper : Helper {
        override fun getViewEnd(recyclerWidth: Int, recyclerHeight: Int): Int = recyclerWidth

        override fun getDistanceToChangeCurrent(childWidth: Int, childHeight: Int): Int = childWidth

        override fun setCurrentViewCenter(recyclerCenter: Point, scrolled: Int, outPoint: Point) {
            val newX = recyclerCenter.x - scrolled
            outPoint[newX] = recyclerCenter.y
        }

        override fun shiftViewCenter(direction: Direction, shiftAmount: Int, outCenter: Point) {
            val newX = outCenter.x + direction.applyTo(shiftAmount)
            outCenter[newX] = outCenter.y
        }

        override fun getFlingVelocity(velocityX: Int, velocityY: Int): Int = velocityX


        override fun getPendingDx(pendingScroll: Int): Int = pendingScroll

        override fun getPendingDy(pendingScroll: Int): Int = 0

        override fun offsetChildren(amount: Int, helper: RecyclerViewProxy) {
            helper.offsetChildrenHorizontal(amount)
        }

        override fun getDistanceFromCenter(
            center: Point,
            viewCenterX: Float,
            viewCenterY: Float
        ): Float {
            return viewCenterX - center.x
        }

        override fun isViewVisible(
            viewCenter: Point,
            halfWidth: Int,
            halfHeight: Int,
            endBound: Int,
            extraSpace: Int
        ): Boolean {
            val viewLeft: Int = viewCenter.x - halfWidth
            val viewRight: Int = viewCenter.x + halfWidth
            return viewLeft < endBound + extraSpace && viewRight > -extraSpace
        }

        override fun hasNewBecomeVisible(lm: CVLayoutManager): Boolean {
            val firstChild: View? = lm.firstChild
            val lastChild: View? = lm.lastChild
            val leftBound: Int = -lm.extraLayoutSpace
            val rightBound: Int = lm.width + lm.extraLayoutSpace

            val isNewVisibleFromLeft = if(firstChild != null)
                lm.getDecoratedLeft(firstChild) > leftBound && lm.getPosition(firstChild) > 0
            else
                false

            val isNewVisibleFromRight = if(lastChild != null)
                lm.getDecoratedRight(lastChild) < rightBound && lm.getPosition(lastChild) < lm.itemCount - 1
            else
                false

            return isNewVisibleFromLeft || isNewVisibleFromRight
        }

        override fun canScrollVertically(): Boolean  = false

        override fun canScrollHorizontally(): Boolean  = true

    }


    class VerticalHelper : Helper {
        override fun getViewEnd(recyclerWidth: Int, recyclerHeight: Int): Int = recyclerHeight

        override fun getDistanceToChangeCurrent(childWidth: Int, childHeight: Int): Int = childHeight

        override fun setCurrentViewCenter(recyclerCenter: Point, scrolled: Int, outPoint: Point) {
            val newY = recyclerCenter.y - scrolled
            outPoint[recyclerCenter.x] = newY
        }

        override fun shiftViewCenter(direction: Direction, shiftAmount: Int, outCenter: Point) {
            val newY = outCenter.y + direction.applyTo(shiftAmount)
            outCenter[outCenter.x] = newY
        }

        override fun getFlingVelocity(velocityX: Int, velocityY: Int): Int = velocityY

        override fun getPendingDx(pendingScroll: Int): Int = 0

        override fun getPendingDy(pendingScroll: Int): Int = pendingScroll

        override fun offsetChildren(amount: Int, helper: RecyclerViewProxy) {
            helper.offsetChildrenVertical(amount)
        }

        override fun getDistanceFromCenter(
            center: Point,
            viewCenterX: Float,
            viewCenterY: Float
        ): Float = viewCenterY - center.y

        override fun isViewVisible(
            viewCenter: Point,
            halfWidth: Int,
            halfHeight: Int,
            endBound: Int,
            extraSpace: Int
        ): Boolean {
            val viewTop: Int = viewCenter.y - halfHeight
            val viewBottom: Int = viewCenter.y + halfHeight
            return viewTop < endBound + extraSpace && viewBottom > -extraSpace
        }

        override fun hasNewBecomeVisible(lm: CVLayoutManager): Boolean {
            val firstChild: View? = lm.firstChild
            val lastChild: View? = lm.lastChild
            val topBound: Int = -lm.extraLayoutSpace
            val bottomBound: Int = lm.height + lm.extraLayoutSpace

            val isNewVisibleFromTop = if(firstChild != null)
                lm.getDecoratedTop(firstChild) > topBound && lm.getPosition(firstChild) > 0
            else
                false

            val isNewVisibleFromBottom = if(lastChild != null)
                lm.getDecoratedBottom(lastChild) < bottomBound && lm.getPosition(lastChild) < lm.itemCount - 1
            else
                false

            return isNewVisibleFromTop || isNewVisibleFromBottom
        }

        override fun canScrollVertically(): Boolean = true

        override fun canScrollHorizontally(): Boolean = false

    }
}
