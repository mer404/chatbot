package com.hashmob.aichat.main.home.tab.generateimage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hashmob.aichat.R
import com.hashmob.aichat.data.model.firebase.data.PromptItem
import com.hashmob.aichat.databinding.ItemPromptBinding
import com.hashmob.aichat.implementor.PromptInterface


class PromptAdapter(
    var context: Context,
    var list: ArrayList<PromptItem>,
    var onClickListener: PromptInterface
) :
    RecyclerView.Adapter<PromptAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemPromptBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun dataBind(model: PromptItem) {
            binding.data = model
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemPromptBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    fun filterList(filterList: ArrayList<PromptItem>) {
            list = filterList
            notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.binding.container.setOnClickListener {
            onClickListener.onClick(position)
            notifyDataSetChanged()
        }
        if (position == selectedPosition) {
            list[position].flag = true
            holder.binding.tvPrompt.setBackgroundResource(R.drawable.item_select)
            holder.binding.tvPrompt.setTextColor(ContextCompat.getColor(context, R.color.black))
        } else {
            list[position].flag = false
            holder.binding.tvPrompt.setBackgroundResource(R.drawable.bg_chat_receiver)
            holder.binding.tvPrompt.setTextColor(ContextCompat.getColor(context, R.color.white))
        }
        data.let { holder.dataBind(it) }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    companion object {
        var selectedPosition = -1
    }
}

