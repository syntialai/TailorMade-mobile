package com.future.tailormade_chat.feature.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.toTimeString
import com.future.tailormade_chat.R
import com.future.tailormade_chat.core.model.entity.Chat
import com.future.tailormade_dls.databinding.LayoutChatContentReplyBinding
import com.future.tailormade_dls.databinding.LayoutChatContentSendBinding

class ChatRoomAdapter(private val userId: String) :
    ListAdapter<Chat, RecyclerView.ViewHolder>(diffCallback) {

  companion object {
    private const val TYPE_SEND = 0
    private const val TYPE_REPLY = 1

    private val diffCallback = object : DiffUtil.ItemCallback<Chat>() {

      override fun areItemsTheSame(oldItem: Chat, newItem: Chat) = oldItem.userId == newItem.userId

      override fun areContentsTheSame(oldItem: Chat, newItem: Chat) = oldItem == newItem
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomSendViewHolder {
    val layout = if (viewType == TYPE_SEND) {
      R.layout.layout_chat_content_send
    } else {
      R.layout.layout_chat_content_reply
    }
    return ChatRoomSendViewHolder(
        LayoutInflater.from(parent.context).inflate(layout, parent, false))
  }

  @RequiresApi(Build.VERSION_CODES.N)
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
      position: Int) {
    if (getItemViewType(position) == TYPE_SEND) {
      (holder as ChatRoomSendViewHolder).bind(getItem(position))
    } else {
      (holder as ChatRoomReplyViewHolder).bind(getItem(position))
    }
  }

  override fun getItemViewType(position: Int): Int = if (isSender(position)) {
    TYPE_SEND
  } else {
    TYPE_REPLY
  }

  private fun isSender(position: Int) = getItem(position).userId == userId

  inner class ChatRoomSendViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val sendBinding = LayoutChatContentSendBinding.bind(view)

    @RequiresApi(Build.VERSION_CODES.N)
    fun bind(data: Chat) {
      with(sendBinding) {
        textViewChatContentSendText.text = data.text.body
        textViewChatContentSendTime.text = data.createdDate.toTimeString(
            Constants.HH_MM)
      }
    }
  }

  inner class ChatRoomReplyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val replyBinding = LayoutChatContentReplyBinding.bind(view)

    @RequiresApi(Build.VERSION_CODES.N)
    fun bind(data: Chat) {
      with(replyBinding) {
        textViewChatContentReplyText.text = data.text.body
        textViewChatContentReplyTime.text = data.createdDate.toTimeString(
            Constants.HH_MM)
      }
    }
  }
}