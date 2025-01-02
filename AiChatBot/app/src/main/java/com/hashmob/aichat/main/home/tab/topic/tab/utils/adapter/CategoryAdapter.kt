package com.hashmob.aichat.main.home.tab.topic.tab.utils.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hashmob.aichat.databinding.ItemCategoriesChatBinding
import com.hashmob.aichat.main.home.tab.topic.tab.utils.model.CategoriesModel

class CategoryAdapter(
    var context: Context,
    var list: ArrayList<CategoriesModel>,
    var onClick: (Int) -> Unit
) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemCategoriesChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun dataBind(model: CategoriesModel) {
            binding.data = model
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCategoriesChatBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.binding.ivCategory.setImageResource(list[position].image)
        holder.binding.view.setOnClickListener {
            onClick.invoke(position)
        }
        data.let { holder.dataBind(it) }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}