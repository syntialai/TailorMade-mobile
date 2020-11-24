package com.future.tailormade_chat.feature.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.toTimeString
import com.future.tailormade_chat.R
import com.future.tailormade_chat.core.model.entity.Chat
import com.future.tailormade_chat.core.model.entity.Session
import com.future.tailormade_dls.databinding.LayoutCardChatBinding

class ChatListAdapter(private val sessionsList: List<Session>) :
    RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChatListViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.layout_card_chat,
          parent, false))

  override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
    holder.bind(sessionsList[position])
  }

  override fun getItemCount(): Int = sessionsList.size

  inner class ChatListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = LayoutCardChatBinding.bind(view)
    private val context = view.context

    fun bind(data: Session) {
      with(binding) {
        textViewChatName.text = data.userName
        textViewChatTime.text = data.updatedDate.toTimeString(Constants.HH_MM)
        textViewChatContent.text = data.chat.text.body
      }
    }
  }
}