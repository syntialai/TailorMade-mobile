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
import com.future.tailormade.util.extension.orTrue
import com.future.tailormade.util.extension.setVisibility
import com.future.tailormade.util.extension.toDateString
import com.future.tailormade_chat.R
import com.future.tailormade_chat.core.model.entity.Session
import com.future.tailormade_chat.databinding.LayoutCardChatBinding

class ChatListAdapter(private val onClickListener: (String, String) -> Unit) :
    ListAdapter<Pair<String, Session>, ChatListAdapter.ChatListViewHolder>(diffCallback) {

  companion object {
    private val diffCallback = object : DiffUtil.ItemCallback<Pair<String, Session>>() {

      override fun areItemsTheSame(
          oldItem: Pair<String, Session>, newItem: Pair<String, Session>) = oldItem.first == newItem.first

      override fun areContentsTheSame(
          oldItem: Pair<String, Session>, newItem: Pair<String, Session>) = oldItem == newItem
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChatListViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.layout_card_chat, parent, false))

  @RequiresApi(Build.VERSION_CODES.N)
  override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  inner class ChatListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = LayoutCardChatBinding.bind(view)
    private val context = view.context

    @RequiresApi(Build.VERSION_CODES.N)
    fun bind(data: Pair<String, Session>) {
      with(binding) {
        with(data.second) {
          textViewChatName.text = userId
          textViewChatTime.text = updatedDate?.toDateString(Constants.HH_MM)
          textViewChatContent.text = chat?.text?.body

          layoutBadge.viewBadge.setVisibility(hasBeenRead.orTrue())

          // TODO: Bind image view
        }

        root.setOnClickListener {
          data.second.userName?.let { name ->
            onClickListener.invoke(data.first, name)
          }
        }
      }
    }
  }
}