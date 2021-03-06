package com.ndsoftwares.carouselview

enum class Direction {
    START {
        override fun applyTo(delta: Int): Int {
            return delta * -1
        }

        override fun sameAs(direction: Int): Boolean {
            return direction < 0
        }

        override fun reverse(): Direction {
            return END
        }
    },
    END {
        override fun applyTo(delta: Int): Int {
            return delta
        }

        override fun sameAs(direction: Int): Boolean {
            return direction > 0
        }

        override fun reverse(): Direction {
            return START
        }
    };

    abstract fun applyTo(delta: Int): Int
    abstract fun sameAs(direction: Int): Boolean
    abstract fun reverse(): Direction?

    companion object {
        fun fromDelta(delta: Int): Direction {
            return if (delta > 0) END else START
        }
    }
}
