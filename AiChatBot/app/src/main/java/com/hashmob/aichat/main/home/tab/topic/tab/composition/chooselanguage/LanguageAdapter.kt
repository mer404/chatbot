package com.hashmob.aichat.main.home.tab.topic.tab.composition.chooselanguage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hashmob.aichat.R
import com.hashmob.aichat.base.BaseApplication.Companion.preferences
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.data.model.firebase.data.LanguageItem
import com.hashmob.aichat.databinding.ItemLanguageBinding

class LanguageAdapter(
    var context: Context,
    var list: ArrayList<LanguageItem>, var onClick: (Int, String) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun dataBind(model: LanguageItem) {
            binding.data = model
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemLanguageBinding.inflate(
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
        Glide
            .with(context)
            .load(list[position].img_link)
            .centerCrop()
            .placeholder(R.drawable.ic_uncheck_premium)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.iv)
        holder.binding.containerChooseLanguage.setOnClickListener {
            onClick.invoke(position, list[position].name.toString())
        }
        selectedPosition = preferences?.getInt(Constants.LANGUAGE_POSITION)!!
        if (position == selectedPosition) {
            list[position].flag = true
            holder.binding.tvLanguage.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorLanguageText
                )
            )
            holder.binding.ivCheck.setImageResource(R.drawable.ic_check)
            holder.binding.containerChooseLanguage.setBackgroundResource(R.drawable.premium_gradient_bg)
        } else {
            list[position].flag = false
            holder.binding.ivCheck.setImageResource(R.drawable.ic_uncheck_premium)
            holder.binding.tvLanguage.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.binding.containerChooseLanguage.setBackgroundResource(R.drawable.frame_bg)
        }
        data.let { holder.dataBind(it) }
    }

    companion object {
        var selectedPosition = 0
    }
}