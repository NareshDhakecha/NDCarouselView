package com.ndsoftwares.carouselview

import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.SmoothScroller

class RecyclerViewProxy(private val layoutManager: RecyclerView.LayoutManager) {
    fun attachView(view: View) {
        layoutManager.attachView(view)
    }

    fun detachView(view: View) {
        layoutManager.detachView(view)
    }

    fun detachAndScrapView(view: View, recycler: Recycler) {
        layoutManager.detachAndScrapView(view, recycler)
    }

    fun detachAndScrapAttachedViews(recycler: Recycler) {
        layoutManager.detachAndScrapAttachedViews(recycler)
    }

    fun recycleView(view: View, recycler: Recycler) {
        recycler.recycleView(view)
    }

    fun removeAndRecycleAllViews(recycler: Recycler) {
        layoutManager.removeAndRecycleAllViews(recycler)
    }

    val childCount: Int
        get() = layoutManager.childCount
    val itemCount: Int
        get() = layoutManager.itemCount

    fun getMeasuredChildForAdapterPosition(position: Int, recycler: Recycler): View {
        val view = recycler.getViewForPosition(position)
        layoutManager.addView(view)
        layoutManager.measureChildWithMargins(view, 0, 0)
        return view
    }

    fun layoutDecoratedWithMargins(v: View, left: Int, top: Int, right: Int, bottom: Int) {
        layoutManager.layoutDecoratedWithMargins(v, left, top, right, bottom)
    }

    fun getChildAt(index: Int): View? {
        return layoutManager.getChildAt(index)
    }

    fun getPosition(view: View): Int {
        return layoutManager.getPosition(view)
    }

    fun getMeasuredWidthWithMargin(child: View): Int {
        val lp = child.layoutParams as MarginLayoutParams
        return layoutManager.getDecoratedMeasuredWidth(child) + lp.leftMargin + lp.rightMargin
    }

    fun getMeasuredHeightWithMargin(child: View): Int {
        val lp = child.layoutParams as MarginLayoutParams
        return layoutManager.getDecoratedMeasuredHeight(child) + lp.topMargin + lp.bottomMargin
    }

    val width: Int
        get() = layoutManager.width
    val height: Int
        get() = layoutManager.height

    fun offsetChildrenHorizontal(amount: Int) {
        layoutManager.offsetChildrenHorizontal(amount)
    }

    fun offsetChildrenVertical(amount: Int) {
        layoutManager.offsetChildrenVertical(amount)
    }

    fun requestLayout() {
        layoutManager.requestLayout()
    }

    fun startSmoothScroll(smoothScroller: SmoothScroller?) {
        layoutManager.startSmoothScroll(smoothScroller)
    }

    fun removeAllViews() {
        layoutManager.removeAllViews()
    }
}
