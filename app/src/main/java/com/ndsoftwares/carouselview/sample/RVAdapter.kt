package com.ndsoftwares.carouselview.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ndsoftwares.carouselview.sample.databinding.ItemListBinding

class RVAdapter(val data: List<String>): RecyclerView.Adapter<RVAdapter.ViewHolder>() {

    private lateinit var parentRecycler: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        parentRecycler = recyclerView
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(

            ItemListBinding.inflate(
                LayoutInflater.from(parent.context
                ), parent, false)

        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding){
            tvTitle.text = data[position]

            root.setOnClickListener(holder)
        }
    }

    override fun getItemCount(): Int = data.size


    inner class ViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        override fun onClick(v: View?) {
            parentRecycler.smoothScrollToPosition(adapterPosition)
        }

    }

}