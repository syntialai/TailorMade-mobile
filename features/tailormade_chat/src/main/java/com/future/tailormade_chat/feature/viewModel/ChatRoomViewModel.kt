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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.ZoneOffset

class ChatRoomViewModel @ViewModelInject constructor(
    private val realtimeDbRepository: RealtimeDbRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  companion object {
    private const val CHAT_ROOM_USER_INFO = "CHAT_ROOM_USER_INFO"
    private const val CHAT_ROOM_ID = "CHAT_ROOM_ID"
    private const val CHAT_ROOM_CONTENT = "CHAT_ROOM_CONTENT"
  }

  private var _anotherUserInfo: Pair<String, String>? = null

  private var _chatRoomId: MutableLiveData<String>

  private var _chatRoomContent: MutableLiveData<Query>
  val chatRoomContent: LiveData<Query>
    get() = _chatRoomContent

  private var _isMessageSent = MutableLiveData<Boolean>()
  val isMessageSent: LiveData<Boolean>
    get() = _isMessageSent

  init {
    _chatRoomId = savedStateHandle.getLiveData(CHAT_ROOM_ID)
    _chatRoomContent = savedStateHandle.getLiveData(CHAT_ROOM_CONTENT)
    _anotherUserInfo = savedStateHandle.get(CHAT_ROOM_USER_INFO)
  }

  override fun getLogName(): String = "com.future.tailormade_chat.feature.viewModel.ChatRoomViewModel"

  fun readChat() {
    authSharedPrefRepository.userId?.let { id ->
      realtimeDbRepository.getUserSession(id, getAnotherUserId())
          .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              if (snapshot.exists()) {
                realtimeDbRepository.updateReadStatus(id, getAnotherUserId())
              }
            }

            override fun onCancelled(error: DatabaseError) {
              // No implementation needed
            }
          })
    }
  }

  fun setChatRoomId(userId: String, userName: String) {
    _anotherUserInfo = Pair(userId, userName)
    _chatRoomId.value = realtimeDbRepository.getRoomId(userId)
    _chatRoomContent.value = realtimeDbRepository.getChatRoomById(_chatRoomId.value.orEmpty())
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun sendMessage(text: String) {
    val nowTimestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
    val anotherUserId = getAnotherUserId()
    val anotherUserName = _anotherUserInfo?.second.orEmpty()
    authSharedPrefRepository.userId?.let { userId ->
      _chatRoomId.value?.let {
        val chat = Chat(nowTimestamp, userId, false, Constants.MESSAGES_TYPE_TEXT, Text(text))
        val userSession = Session(nowTimestamp, anotherUserId, anotherUserName, chat, true)
        val anotherUserSession = userSession.copy().apply {
          this.userId = userId
          this.userName = authSharedPrefRepository.name
          this.hasBeenRead = false
        }
        realtimeDbRepository
            .addChatRoomAndSession(it, userSession, anotherUserSession, chat).addOnSuccessListener {
              setIsSent(true)
            }.addOnFailureListener {
              setIsSent(false)
            }
      }
    }
  }

  fun setIsSent(value: Boolean?) {
    _isMessageSent.value = value
  }

  private fun getAnotherUserId() = _anotherUserInfo?.first.orEmpty()
}