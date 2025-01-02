package com.hashmob.aichat.main.home.tab.chatbot

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.hashmob.aichat.R
import com.hashmob.aichat.data.model.apimodel.ChoicesItem

class ChatAdapter(
    var context: Context,
    private var chatList: ArrayList<ChoicesItem>,
    private var copyClick: (Int) -> Unit,
    private var forwardClick: (Int) -> Unit,
    private var speechClick: (Int, ViewHolder) -> Unit,
    private var pauseClick: (ViewHolder) -> Unit
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var message: AppCompatTextView = view.findViewById(R.id.tvMessage)
        var forward = view.findViewById<AppCompatImageView>(R.id.ivForward)
        var ivPlay = view.findViewById<AppCompatImageView>(R.id.ivPlay)
        var ivPause = view.findViewById<AppCompatImageView>(R.id.ivPause)
        var copy = view.findViewById<AppCompatImageView>(R.id.ivCopy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (chatList[viewType].isSend!!) {
            ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_send_message, parent, false)
            )
        } else {
            ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_receive_message, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (chatList[position].isSend!!) {
            holder.message.text = chatList[position].text
        } else {
            holder.message.text = chatList[position].text?.trim()
            holder.copy.setOnClickListener {
                copyClick.invoke(position)
            }
            if (chatList[position].isPlaying) {
                holder.ivPlay.visibility = View.INVISIBLE
                holder.ivPause.visibility = View.VISIBLE
            } else {
                holder.ivPause.visibility = View.INVISIBLE
                holder.ivPlay.visibility = View.VISIBLE
            }
            holder.ivPlay.setOnClickListener {
                speechClick.invoke(position, holder)
            }
            holder.ivPause.setOnClickListener {
                pauseClick.invoke(holder)
            }
            holder.forward.setOnClickListener {
                forwardClick.invoke(position)
            }
      }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

