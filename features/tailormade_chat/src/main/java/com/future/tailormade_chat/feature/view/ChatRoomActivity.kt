package com.future.tailormade_chat.feature.view

import android.os.Bundle
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade_chat.R
import com.future.tailormade_chat.databinding.ActivityChatRoomBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatRoomActivity : BaseActivity() {

  private lateinit var binding: ActivityChatRoomBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityChatRoomBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarChatRoom
    setContentView(binding.root)
  }

  companion object {
    private const val PARAM_CHAT_ROOM_ID = "PARAM_CHAT_ROOM_ID"
  }
}