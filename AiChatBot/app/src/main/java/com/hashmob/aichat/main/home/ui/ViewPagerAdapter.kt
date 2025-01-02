package com.hashmob.aichat.main.home.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hashmob.aichat.databinding.ItemHomeViewpagerBinding


class ViewPagerAdapter(
    var context: Context,
    var list: ArrayList<ViewPagerModel>,
    var onClick: (Int) -> Unit
) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemHomeViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var btnGetStarted = binding.btnGetStarted
        fun dataBind(model: ViewPagerModel) {
            binding.data = model
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemHomeViewpagerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.btnGetStarted.setOnClickListener {
            onClick.invoke(position)
        }
        holder.binding.iv.setImageResource(list[position].image)
        data.let { holder.dataBind(it) }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

