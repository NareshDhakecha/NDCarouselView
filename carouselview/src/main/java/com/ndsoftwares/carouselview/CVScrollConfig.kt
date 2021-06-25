package com.ndsoftwares.carouselview

enum class CVScrollConfig {

    ENABLED {
        override fun isScrollBlocked(direction: Direction): Boolean  = false
    },
    FORWARD_ONLY {
        override fun isScrollBlocked(direction: Direction): Boolean
            = direction === Direction.START
    },
    BACKWARD_ONLY {
        override fun isScrollBlocked(direction: Direction): Boolean = direction === Direction.END
    },
    DISABLED {
        override fun isScrollBlocked(direction: Direction): Boolean  = true
    };

    abstract fun isScrollBlocked(direction: Direction): Boolean
}