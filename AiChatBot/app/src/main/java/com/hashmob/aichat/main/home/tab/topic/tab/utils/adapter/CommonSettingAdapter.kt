package com.hashmob.aichat.main.home.tab.topic.tab.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.hashmob.aichat.databinding.ItemTopicSettingBinding
import com.hashmob.aichat.main.home.tab.topic.tab.utils.model.TopicSettingModel

class CommonSettingAdapter(
    var activity: AppCompatActivity,
    var list: ArrayList<TopicSettingModel>,
    var onClick: (Int) -> Unit
) : RecyclerView.Adapter<CommonSettingAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemTopicSettingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun dataBind(model: TopicSettingModel) {
            binding.data = model
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemTopicSettingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.binding.iv.setImageResource(list[position].image)
        holder.binding.containerChooseLanguage.setOnClickListener {
            onClick.invoke(position)
        }
        data.let { holder.dataBind(it) }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}