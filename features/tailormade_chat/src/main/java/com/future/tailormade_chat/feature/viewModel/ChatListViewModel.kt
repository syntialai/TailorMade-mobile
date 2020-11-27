package com.future.tailormade_chat.feature.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import com.future.tailormade_chat.core.repository.RealtimeDbRepository
import com.google.firebase.database.Query

class ChatListViewModel @ViewModelInject constructor(
    private val realtimeDbRepository: RealtimeDbRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository) :
    BaseViewModel() {

  override fun getLogName(): String =
      "com.future.tailormade_chat.feature.viewModel.ChatListViewModel"

  fun getChatRooms() = realtimeDbRepository.getChatRooms()

  fun getUserChatSessions(): Query? = authSharedPrefRepository.userId?.let { userId ->
    realtimeDbRepository.getUserChatSessionById(userId)
  }
}