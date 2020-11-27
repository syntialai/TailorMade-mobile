package com.future.tailormade_chat.feature.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade_chat.core.model.entity.ChatRoom
import com.future.tailormade_chat.databinding.FragmentChatListBinding
import com.future.tailormade_chat.feature.adapter.ChatListAdapter
import com.future.tailormade_chat.feature.viewModel.ChatListViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListFragment : BaseFragment() {

  private lateinit var binding: FragmentChatListBinding

  private val viewModel: ChatListViewModel by viewModels()

  private val adapterValueEventListener by lazy {
    object : ValueEventListener {

      @RequiresApi(Build.VERSION_CODES.N)
      override fun onDataChange(snapshot: DataSnapshot) {
        val chatRoom = viewModel.mapLastChatInChatRoom(
            snapshot.value as Map<String, ChatRoom>)
        adapter.submitList(chatRoom)
      }

      override fun onCancelled(error: DatabaseError) {
        // TODO: Provide error
      }
    }
  }

  private var adapter = ChatListAdapter()

  override fun getScreenName(): String = "Chat"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentChatListBinding.inflate(inflater, container, false)

    with(binding) {
      recyclerViewChatList.layoutManager = LinearLayoutManager(context)
      recyclerViewChatList.adapter = adapter
    }

    setupListener()

    return binding.root
  }

  private fun setupListener() {
    viewModel.getChatRooms().addValueEventListener(adapterValueEventListener)
  }

  override fun onDestroyView() {
    viewModel.getChatRooms().removeEventListener(adapterValueEventListener)
    super.onDestroyView()
  }

  companion object {

    @JvmStatic fun newInstance() = ChatListFragment()
  }
}