package com.future.tailormade_chat.feature.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import com.future.tailormade_chat.core.model.entity.ChatRoom
import com.future.tailormade_chat.core.repository.RealtimeDbRepository
import com.google.firebase.database.Query

class ChatRoomViewModel @ViewModelInject constructor(
    private val realtimeDbRepository: RealtimeDbRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository
) : BaseViewModel() {

  private var _chatRoomId = MutableLiveData<String>()
  val chatRoomId: LiveData<String>
    get() = _chatRoomId

  private var _chatRoomContent = MutableLiveData<Query>()
  val chatRoomContent: LiveData<Query>
    get() = _chatRoomContent

  override fun getLogName(): String =
      "com.future.tailormade_chat.feature.viewModel.ChatRoomViewModel"

  fun fetchChatRoomData() {
    _chatRoomId.value?.let {
      _chatRoomContent.value = realtimeDbRepository.getChatRooms()
    }
  }

  fun setChatRoomId(chatRoomId: String) {
    _chatRoomId.value = chatRoomId
  }
}