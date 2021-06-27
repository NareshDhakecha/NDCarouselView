package com.ndsoftwares.carouselview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import com.ndsoftwares.carouselview.transform.CVScrollItemTransformer

class NDCarouselView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyle: Int = 0
): RecyclerView(context, attrs, defStyle) {

    private val onItemChangedListeners = mutableListOf<OnItemChangedListener<ViewHolder>>()
    private val scrollStateChangeListeners = mutableListOf<ScrollStateChangeListener<ViewHolder>>()
    private var cvLayoutManager: CVLayoutManager
    private var isOverScrollEnabled = false

    var colorSelectedBackground: Int = Color.GRAY


    private val notifyItemChangedRunnable = Runnable { notifyCurrentItemChanged() }

    companion object{
        private val DEFAULT_ORIENTATION: Int = CVOrientation.HORIZONTAL.ordinal

    }

    init {
        var orientation: Int = DEFAULT_ORIENTATION
        if (attrs != null) {
            val ta = getContext().obtainStyledAttributes(attrs, R.styleable.NDCarouselView)
            orientation = ta.getInt(
                R.styleable.NDCarouselView_carousel_orientation,
                DEFAULT_ORIENTATION
            )

            colorSelectedBackground = ta.getColor(
                R.styleable.NDCarouselView_carousel_selectedBackgroundColor,
                Color.LTGRAY
            )

            ta.recycle()
        }

        isOverScrollEnabled = overScrollMode != OVER_SCROLL_NEVER

        cvLayoutManager = CVLayoutManager(
            getContext(), ScrollStateListener(),
            CVOrientation.values()[orientation],
            colorSelectedBackground
        )

        layoutManager = cvLayoutManager
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        if (layout is CVLayoutManager) {
            super.setLayoutManager(layout)
        } else {
            throw IllegalArgumentException(context.getString(R.string.dsv_ex_msg_dont_set_lm))
        }
    }

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        if (cvLayoutManager.isFlingDisallowed(velocityX, velocityY)) {
            return false
        }
        val isFling = super.fling(velocityX, velocityY)
        if (isFling) {
            cvLayoutManager.onFling(velocityX, velocityY)
        } else {
            cvLayoutManager.returnToCurrentPosition()
        }
        return isFling
    }

    fun getViewHolder(position: Int): ViewHolder? {
        val view = cvLayoutManager.findViewByPosition(position)
        return view?.let { getChildViewHolder(it) }
    }

    override fun scrollToPosition(position: Int) {
        val currentPosition: Int = cvLayoutManager.currentPosition

        super.scrollToPosition(position)
        if (currentPosition != position) {
            notifyCurrentItemChanged()
        }
    }

    /**
     * @return adapter position of the current item or -1 if nothing is selected
     */
    fun getCurrentItem(): Int = cvLayoutManager.currentPosition

    fun setItemTransformer(transformer: CVScrollItemTransformer) {
        cvLayoutManager.setItemTransformer(transformer)
    }

    fun setItemTransitionTimeMillis(@IntRange(from = 10) millis: Int) {
        cvLayoutManager.setTimeForItemSettle(millis)
    }

    fun setSlideOnFling(result: Boolean) {
        cvLayoutManager.setShouldSlideOnFling(result)
    }

    fun setSlideOnFlingThreshold(threshold: Int) {
        cvLayoutManager.setSlideOnFlingThreshold(threshold)
    }

    fun setOrientation(orientation: CVOrientation) {
        cvLayoutManager.setOrientation(orientation)
    }

    fun setOffscreenItems(items: Int) {
        cvLayoutManager.setOffscreenItems(items)
    }

    fun setScrollConfig(config: CVScrollConfig) {
        cvLayoutManager.setScrollConfig(config)
    }

    fun setClampTransformProgressAfter(@IntRange(from = 1) itemCount: Int) {
        require(itemCount > 1) { "must be >= 1" }
        cvLayoutManager.setTransformClampItemCount(itemCount)
    }

    fun setOverScrollEnabled(overScrollEnabled: Boolean) {
        isOverScrollEnabled = overScrollEnabled
        overScrollMode = OVER_SCROLL_NEVER
    }

    fun addScrollStateChangeListener(scrollStateChangeListener: ScrollStateChangeListener<ViewHolder>) {
        scrollStateChangeListeners.add(scrollStateChangeListener)
    }

    fun addScrollListener(scrollListener: ScrollListener<ViewHolder>) {
        addScrollStateChangeListener(ScrollListenerAdapter(scrollListener))
    }

    fun addOnItemChangedListener(onItemChangedListener: OnItemChangedListener<ViewHolder>) {
        onItemChangedListeners.add(onItemChangedListener)
    }

    fun removeScrollStateChangeListener(scrollStateChangeListener: ScrollStateChangeListener<*>) {
        scrollStateChangeListeners.remove(scrollStateChangeListener)
    }

    fun removeScrollListener(scrollListener: ScrollListener<*>) {
        removeScrollStateChangeListener(ScrollListenerAdapter(scrollListener))
    }

    fun removeItemChangedListener(onItemChangedListener: OnItemChangedListener<*>) {
        onItemChangedListeners.remove(onItemChangedListener)
    }

    private fun notifyScrollStart(holder: ViewHolder, current: Int) {
        for (listener in scrollStateChangeListeners) {
            listener.onScrollStart(holder, current)
        }
    }

    private fun notifyScrollEnd(holder: ViewHolder, current: Int) {
        for (listener in scrollStateChangeListeners) {
            listener.onScrollEnd(holder, current)
        }
    }

    private fun notifyScroll(
        position: Float,
        currentIndex: Int, newIndex: Int,
        currentHolder: ViewHolder, newHolder: ViewHolder
    ) {


        for (listener in scrollStateChangeListeners) {
            listener.onScroll(
                position, currentIndex, newIndex,
                currentHolder,
                newHolder
            )
        }
    }


    private fun notifyCurrentItemChanged(holder: ViewHolder, current: Int) {
        for (listener in onItemChangedListeners) {
            listener.onCurrentItemChanged(holder, current)
        }
    }

    private fun notifyCurrentItemChanged() {
        removeCallbacks(notifyItemChangedRunnable)
        if (onItemChangedListeners.isEmpty()) {
            return
        }
        val current: Int = cvLayoutManager.currentPosition
        val currentHolder = getViewHolder(current)
        currentHolder?.let { notifyCurrentItemChanged(it, current) } ?: post(
            notifyItemChangedRunnable
        )
    }


    inner class ScrollStateListener : CVLayoutManager.ScrollStateListener {

        override fun onIsBoundReachedFlagChange(isBoundReached: Boolean) {
            if (isOverScrollEnabled) {
                overScrollMode = if (isBoundReached) OVER_SCROLL_ALWAYS else OVER_SCROLL_NEVER
            }
        }

        override fun onScrollStart() {
            removeCallbacks(notifyItemChangedRunnable)
            if (scrollStateChangeListeners.isEmpty()) {
                return
            }
            val current: Int = cvLayoutManager.currentPosition
            val holder: ViewHolder? = getViewHolder(current)
            if (holder != null) {
                notifyScrollStart(holder, current)
            }
        }

        override fun onScrollEnd() {
            if (onItemChangedListeners.isEmpty() && scrollStateChangeListeners.isEmpty()) {
                return
            }
            val current: Int = cvLayoutManager.currentPosition
            val holder: ViewHolder? = getViewHolder(current)
            if (holder != null) {
                notifyScrollEnd(holder, current)
                notifyCurrentItemChanged(holder, current)
            }
        }

        override fun onScroll(currentViewPosition: Float) {

            if (scrollStateChangeListeners.isEmpty()) {
                return
            }
            val currentIndex: Int = getCurrentItem()
            val newIndex: Int = cvLayoutManager.nextPosition
            if (currentIndex != newIndex) {
                getViewHolder(currentIndex)?.let { currentHolder ->
                    getViewHolder(newIndex)?.let { newHolder ->
                        notifyScroll(
                            currentViewPosition,
                            currentIndex, newIndex,
                            currentHolder,
                            newHolder
                        )
                    }
                }
            }
        }

        override fun onCurrentViewFirstLayout() {
            notifyCurrentItemChanged()
        }

        override fun onDataSetChangeChangedPosition() {
            notifyCurrentItemChanged()
        }
    }


    interface ScrollStateChangeListener<T: ViewHolder> {
        fun onScrollStart(currentItemHolder: T, adapterPosition: Int)
        fun onScrollEnd(currentItemHolder: T, adapterPosition: Int)
        fun onScroll(
            scrollPosition: Float,
            currentPosition: Int,
            newPosition: Int,
            currentHolder: T,
            newCurrent: T
        )
    }

    interface ScrollListener<T: ViewHolder> {
        fun onScroll(
            scrollPosition: Float,
            currentPosition: Int, newPosition: Int,
            currentHolder: T,
            newCurrent: T
        )
    }

    interface OnItemChangedListener<T : ViewHolder> {
        /*
         * This method will be also triggered when view appears on the screen for the first time.
         * If data set is empty, viewHolder will be null and adapterPosition will be NO_POSITION
         */
        fun onCurrentItemChanged(viewHolder: T, adapterPosition: Int)
    }

}