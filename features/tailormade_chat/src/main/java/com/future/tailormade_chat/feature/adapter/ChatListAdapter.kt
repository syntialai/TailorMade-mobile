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
import com.future.tailormade.util.extension.getFirstElement
import com.future.tailormade.util.extension.setVisibility
import com.future.tailormade.util.extension.toTimeString
import com.future.tailormade_chat.R
import com.future.tailormade_chat.core.model.entity.ChatRoom
import com.future.tailormade_dls.databinding.LayoutCardChatBinding

class ChatListAdapter :
    ListAdapter<Map.Entry<String, ChatRoom>, ChatListAdapter.ChatListViewHolder>(
        diffCallback) {

  companion object {
    private val diffCallback = object :
        DiffUtil.ItemCallback<Map.Entry<String, ChatRoom>>() {

      override fun areItemsTheSame(oldItem: Map.Entry<String, ChatRoom>,
          newItem: Map.Entry<String, ChatRoom>): Boolean = oldItem == newItem

      override fun areContentsTheSame(oldItem: Map.Entry<String, ChatRoom>,
          newItem: Map.Entry<String, ChatRoom>): Boolean = oldItem == newItem
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChatListViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.layout_card_chat,
          parent, false))

  @RequiresApi(Build.VERSION_CODES.N)
  override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class ChatListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = LayoutCardChatBinding.bind(view)
    private val context = view.context

    @RequiresApi(Build.VERSION_CODES.N)
    fun bind(data: Map.Entry<String, ChatRoom>) {
      val chat = data.value.chats.getFirstElement().value
      val user = data.value.users.getFirstElement()

      with(binding) {
        textViewChatName.text = user.value.name
        textViewChatTime.text = chat.createdDate.toTimeString(Constants.HH_MM)
        textViewChatContent.text = chat.text.body

        layoutBadge.viewBadge.setVisibility(chat.hasBeenRead)

        // TODO: Bind image view
      }
    }
  }
}