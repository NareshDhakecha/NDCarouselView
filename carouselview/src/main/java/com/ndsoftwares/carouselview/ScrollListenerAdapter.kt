package com.ndsoftwares.carouselview

import androidx.recyclerview.widget.RecyclerView

class ScrollListenerAdapter<T : RecyclerView.ViewHolder>(var adaptee: NDCarouselView.ScrollListener<T>) :
    NDCarouselView.ScrollStateChangeListener<T> {

    override fun onScrollStart(currentItemHolder: T, adapterPosition: Int) {}
    override fun onScrollEnd(currentItemHolder: T, adapterPosition: Int) {}
    override fun onScroll(
        scrollPosition: Float,
        currentPosition: Int, newPosition: Int,
        currentHolder: T, newCurrent: T
    ) {
        adaptee.onScroll(scrollPosition, currentPosition, newPosition, currentHolder, newCurrent)
    }

    override fun equals(other: Any?): Boolean {
        return if (other is ScrollListenerAdapter<*>) {
            adaptee == other.adaptee
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return adaptee.hashCode()
    }

}
