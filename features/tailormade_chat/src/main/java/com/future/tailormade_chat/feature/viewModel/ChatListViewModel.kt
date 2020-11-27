package com.future.tailormade_chat.feature.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.getFirstElement
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import com.future.tailormade_chat.core.model.entity.ChatRoom
import com.future.tailormade_chat.core.repository.RealtimeDbRepository

class ChatListViewModel @ViewModelInject constructor(
    private val realtimeDbRepository: RealtimeDbRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository
    ) : BaseViewModel() {

  override fun getLogName(): String =
      "com.future.tailormade_chat.feature.viewModel.ChatListViewModel"

  fun getChatRooms() = realtimeDbRepository.getChatRooms()

  @RequiresApi(Build.VERSION_CODES.N)
  fun mapLastChatInChatRoom(chatRoomList: Map<String, ChatRoom>): List<Map.Entry<String, ChatRoom>> {
    return chatRoomList.map { chatRoom ->
      val lastChat = chatRoom.value.chats.getFirstElement()
      chatRoom.value.chats[lastChat.key] = lastChat.value
      chatRoom.value.users.keys.removeIf { it != authSharedPrefRepository.userId }
      chatRoom
    }
  }
}