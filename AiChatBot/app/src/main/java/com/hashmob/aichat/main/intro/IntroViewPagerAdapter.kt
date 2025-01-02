package com.hashmob.aichat.main.intro

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hashmob.aichat.databinding.ItemIntroBinding

class IntroViewPagerAdapter(
    var context: Context,
    var list: ArrayList<IntroModel>,
) : RecyclerView.Adapter<IntroViewPagerAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemIntroBinding) : RecyclerView.ViewHolder(binding.root) {
        fun dataBind(model: IntroModel) {
            binding.data = model
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemIntroBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.binding.iv.setImageResource(list[position].image)
        data.let { holder.dataBind(it) }
    }
}