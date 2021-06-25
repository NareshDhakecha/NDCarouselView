package com.ndsoftwares.carouselview.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ndsoftwares.carouselview.NDCarouselView
import com.ndsoftwares.carouselview.transform.ScaleTransformer
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var itemPicker: NDCarouselView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemPicker = findViewById<NDCarouselView>(R.id.item_picker)

//        itemPicker.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        itemPicker.setSlideOnFling(true)
        itemPicker.adapter = RVAdapter(getData())

//        itemPicker.addOnItemChangedListener(this)
//        itemPicker.addScrollStateChangeListener(this)
        itemPicker.scrollToPosition(2)
        itemPicker.setItemTransitionTimeMillis(150)
        itemPicker.setItemTransformer(
            ScaleTransformer.Builder()
                .setMinScale(0.6f)
                .build()
        )
    }

    private fun getData(): List<String> =
        listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6")
}