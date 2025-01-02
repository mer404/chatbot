package com.hashmob.aichat.main.home.premium

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hashmob.aichat.R
import com.hashmob.aichat.databinding.ItemPremiumBinding

class PremiumAdapter(
    var context: Context,
    var list: ArrayList<PremiumModel>
) : RecyclerView.Adapter<PremiumAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemPremiumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun dataBind(model: PremiumModel) {
            binding.data = model
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemPremiumBinding.inflate(
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
        holder.binding.containerPerWeek.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
        }
        if (position == selectedPosition) {
            list[position].flag = true
            holder.binding.tvEnd.background= AppCompatResources.getDrawable(context,R.drawable.bg_premium_tag)
            holder.binding.tvPerWeek.setTextColor(ContextCompat.getColor(context, R.color.colorPremiumText))
            holder.binding.tvPerWeekPrice.setTextColor(ContextCompat.getColor(context, R.color.colorPremiumText))
            holder.binding.containerPerWeek.setBackgroundResource(R.drawable.premium_gradient_bg)
        } else {
            list[position].flag = false
            holder.binding.tvPerWeek.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.binding.tvPerWeekPrice.setTextColor(ContextCompat.getColor(context, R.color.white))
            holder.binding.tvEnd.background= AppCompatResources.getDrawable(context,R.drawable.bg_premium_tag_unselected)
            holder.binding.containerPerWeek.setBackgroundResource(R.drawable.frame_bg)
        }
        data.let { holder.dataBind(it) }
    }

    companion object {
        var selectedPosition = 0
    }
}