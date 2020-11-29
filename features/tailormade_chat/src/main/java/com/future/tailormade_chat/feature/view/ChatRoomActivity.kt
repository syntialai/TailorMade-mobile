package com.future.tailormade_chat.feature.view

import android.os.Bundle
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade_chat.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatRoomActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat_room)
  }

  companion object {
    private const val PARAM_CHAT_ROOM_ID = "PARAM_CHAT_ROOM_ID"
  }
}