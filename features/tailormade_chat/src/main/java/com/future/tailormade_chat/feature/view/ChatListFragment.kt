package com.future.tailormade_chat.feature.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade_chat.databinding.FragmentChatListBinding
import com.future.tailormade_chat.feature.viewModel.ChatListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListFragment : BaseFragment() {

  private lateinit var binding: FragmentChatListBinding

  private val viewModel: ChatListViewModel by viewModels()

  override fun getScreenName(): String = "Chat"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentChatListBinding.inflate(inflater, container, false)

    with(binding) {
      recyclerViewChatList.layoutManager = LinearLayoutManager(context)
    }

    return binding.root
  }

  private fun setupAdapter() {
    // TODO: setup chat list adapter
  }

  private fun setupFragmentObserver() {
    // TODO: make this function overriding base fragment setup observer
  }

  companion object {

    @JvmStatic
    fun newInstance() = ChatListFragment()
  }
}