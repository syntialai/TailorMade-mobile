package com.future.tailormade_chat.feature.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.setVisibility
import com.future.tailormade.util.extension.toTimeString
import com.future.tailormade_chat.R
import com.future.tailormade_chat.core.model.entity.Session
import com.future.tailormade_chat.databinding.LayoutCardChatBinding

class ChatListAdapter :
    ListAdapter<Session, ChatListAdapter.ChatListViewHolder>(diffCallback) {

  companion object {
    private val diffCallback = object : DiffUtil.ItemCallback<Session>() {

      override fun areItemsTheSame(oldItem: Session, newItem: Session) = oldItem.userId == newItem.userId

      override fun areContentsTheSame(oldItem: Session, newItem: Session) = oldItem == newItem
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
    fun bind(data: Session) {
      with(binding) {
        textViewChatName.text = data.userId
        textViewChatTime.text = data.updatedDate.toTimeString(Constants.HH_MM)
        textViewChatContent.text = data.chat.text.body

        layoutBadge.viewBadge.setVisibility(data.hasBeenRead)

        // TODO: Bind image view
      }
    }
  }
}