package com.future.tailormade_chat.feature.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade_chat.core.model.entity.Chat
import com.future.tailormade_chat.core.model.entity.Session
import com.future.tailormade_chat.core.model.entity.Text
import com.future.tailormade_chat.core.repository.RealtimeDbRepository
import com.google.firebase.database.Query
import java.time.LocalDateTime
import java.time.ZoneOffset

class ChatRoomViewModel @ViewModelInject constructor(
    private val realtimeDbRepository: RealtimeDbRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  companion object {
    private const val CHAT_ROOM_USER_ID = "CHAT_ROOM_USER_ID"
    private const val CHAT_ROOM_ID = "CHAT_ROOM_ID"
    private const val CHAT_ROOM_CONTENT = "CHAT_ROOM_CONTENT"
  }

  private var anotherUserId: String? = null

  private var _chatRoomId: MutableLiveData<String>
  val chatRoomId: LiveData<String>
    get() = _chatRoomId

  private var _chatRoomContent: MutableLiveData<Query>
  val chatRoomContent: LiveData<Query>
    get() = _chatRoomContent

  private var _isMessageSent = MutableLiveData<Boolean>()
  val isMessageSent: LiveData<Boolean>
    get() = _isMessageSent

  init {
    _chatRoomId = savedStateHandle.getLiveData(CHAT_ROOM_ID)
    _chatRoomContent = savedStateHandle.getLiveData(CHAT_ROOM_CONTENT)
    anotherUserId = savedStateHandle.get(CHAT_ROOM_USER_ID)
  }

  override fun getLogName(): String = "com.future.tailormade_chat.feature.viewModel.ChatRoomViewModel"

  fun fetchChatRoomData() {
    _chatRoomId.value?.let {
      _chatRoomContent.value = realtimeDbRepository.getChatRooms()
    }
  }

  fun setChatRoomId(userId: String) {
    anotherUserId = userId
    _chatRoomId.value = realtimeDbRepository.getRoomId(userId)
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun sendMessage(text: String) {
    val nowTimestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
    authSharedPrefRepository.userId?.let { userId ->
      _chatRoomId.value?.let {
        val chat = Chat(nowTimestamp, userId, false, Constants.MESSAGES_TYPE_TEXT, Text(text))
        val session = Session(nowTimestamp, userId, authSharedPrefRepository.name, chat, false)
        realtimeDbRepository.addChatRoomAndSession(
            anotherUserId.orEmpty(), it, session, chat)?.addOnSuccessListener {
          setIsSent(true)
        }?.addOnFailureListener {
          setIsSent(false)
        } ?: setIsSent(false)
      }
    }
  }

  fun setIsSent(value: Boolean?) {
    _isMessageSent.value = value
  }
}