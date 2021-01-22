package com.future.tailormade_chat.feature.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade_chat.core.model.entity.Chat
import com.future.tailormade_chat.core.model.entity.Text
import com.future.tailormade_chat.core.repository.RealtimeDbRepository
import com.google.firebase.database.Query
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset

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

  override fun getLogName(): String = "com.future.tailormade_chat.feature.viewModel.ChatRoomViewModel"

  fun fetchChatRoomData() {
    _chatRoomId.value?.let {
      _chatRoomContent.value = realtimeDbRepository.getChatRooms()
    }
  }

  fun setChatRoomId(chatRoomId: String) {
    _chatRoomId.value = chatRoomId
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun sendMessage(text: String) {
    authSharedPrefRepository.userId?.let { userId ->
      _chatRoomId.value?.let {
        val chat = Chat(Timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)), userId, false,
            Constants.MESSAGES_TYPE_TEXT, Text(text))
        realtimeDbRepository.updateChatRoom(it, chat)
      }
    }
  }
}