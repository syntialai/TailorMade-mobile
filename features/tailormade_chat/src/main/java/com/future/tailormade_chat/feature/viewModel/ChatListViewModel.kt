package com.future.tailormade_chat.feature.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade_chat.core.repository.RealtimeDbRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.database.Query

class ChatListViewModel @ViewModelInject constructor(
    private val realtimeDbRepository: RealtimeDbRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository) :
    BaseViewModel() {

  override fun getLogName(): String =
      "com.future.tailormade_chat.feature.viewModel.ChatListViewModel"

  fun getChatRooms() = realtimeDbRepository.getChatRooms()

  fun getChatRoomId(userChatId: String): String? {
    return authSharedPrefRepository.userId?.let {
      return@let if (authSharedPrefRepository.userRole == 0) {
        "${it}_$userChatId"
      } else {
        "${userChatId}_${it}"
      }
    }
  }

  fun getUserChatSessions(): Query? = authSharedPrefRepository.userId?.let { userId ->
    realtimeDbRepository.getUserChatSessionById(userId)
  }

  fun deleteSessionByUserChatSession(userChatId: String): Task<Void>? {
    return authSharedPrefRepository.userId?.let { userId ->
      realtimeDbRepository.deleteSessionByUserChatSession(userId, userChatId)
    }
  }
}