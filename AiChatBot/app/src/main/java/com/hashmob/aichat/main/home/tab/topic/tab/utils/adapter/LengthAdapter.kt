package com.hashmob.aichat.main.home.tab.topic.tab.utils.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hashmob.aichat.base.BaseApplication.Companion.preferences
import com.hashmob.aichat.constants.Constants
import com.hashmob.aichat.databinding.ItemSelectLengthBinding
import com.hashmob.aichat.main.home.tab.topic.tab.utils.model.LengthModel

class LengthAdapter(
    var context: Context,
    var list: ArrayList<LengthModel>, var positionClick: (Int) -> Unit
) : RecyclerView.Adapter<LengthAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemSelectLengthBinding) : RecyclerView.ViewHolder(binding.root) {
        fun dataBind(model: LengthModel) {
            binding.data = model
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemSelectLengthBinding.inflate(
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
        holder.binding.tvLength.setOnClickListener {
            selectedPosition = position
            positionClick.invoke(selectedPosition)
            notifyDataSetChanged()
            selectedPosition = preferences?.getInt(Constants.LENGTH_POSITION)!!
        }
        if (position == preferences?.getInt(Constants.LENGTH_POSITION)!!) {
            holder.binding.ivCheck.visibility = View.VISIBLE
        } else {
            holder.binding.ivCheck.visibility = View.INVISIBLE
        }
        data.let { holder.dataBind(it) }
    }

    companion object {
        var selectedPosition = preferences?.getInt(Constants.LENGTH_POSITION)!!
    }
}