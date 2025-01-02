package com.hashmob.aichat.main.home.tab.generateimage.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hashmob.aichat.R
import com.hashmob.aichat.data.model.firebase.data.StyleItem
import com.hashmob.aichat.databinding.ItemFilterArtBinding

class ArtStyleAdapter(var context: Context, var list: ArrayList<StyleItem>) :
    RecyclerView.Adapter<ArtStyleAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemFilterArtBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun dataBind(model: StyleItem) {
            binding.data = model
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemFilterArtBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        Glide
            .with(context)
            .load(list[position].img_link)
            .centerCrop()
            .placeholder(R.drawable.ic_no_filter)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.ivFilter)
        holder.binding.ivFilter.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
        }
        if (position == selectedPosition) {
            list[position].flag = true
            holder.binding.view.setBackgroundResource(R.drawable.select_frame_bg)
        } else {
            list[position].flag = false
            holder.binding.view.setBackgroundResource(R.drawable.frame_bg)
        }
        data.let { holder.dataBind(it) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    companion object {
        var selectedPosition = 0
    }

}

