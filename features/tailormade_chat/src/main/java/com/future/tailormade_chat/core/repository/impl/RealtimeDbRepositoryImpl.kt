package com.future.tailormade_chat.core.repository.impl

import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import com.future.tailormade_chat.core.config.ReferenceConstants
import com.future.tailormade_chat.core.model.entity.Chat
import com.future.tailormade_chat.core.model.entity.ChatRoom
import com.future.tailormade_chat.core.repository.RealtimeDbRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import javax.inject.Inject

class RealtimeDbRepositoryImpl @Inject constructor(
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    databaseReference: DatabaseReference) : RealtimeDbRepository {

  private val getChatRoomRef = databaseReference.child(
      ReferenceConstants.CHAT_ROOMS)

  override fun getChatRooms(): Query {
    if (authSharedPrefRepository.userRole == 0) {
      return getChatRoomRef.startAt(authSharedPrefRepository.userId)
    }
    return getChatRoomRef.endAt(authSharedPrefRepository.userId)
  }

  override fun addChatRoom(anotherUserId: String, chatRoom: ChatRoom): Task<Void> {
    val roomId = getRoomId(anotherUserId)
    return setValue(getChatRoomRef.child(roomId), chatRoom)
  }

  override fun updateChatRoom(chatRoomId: String, chat: Chat): Task<Void> {
    // TODO: Change chatId to real generated chatId
    return getRoomChatsById(chatRoomId).updateChildren(
        mutableMapOf<String, Any>(Pair("chatId", chat)))
  }

  override fun deleteChatRoom(chatRoomId: String): Task<Void> {
    return setValue(getChatRoomRef.child(chatRoomId), null)
  }

  private fun getRoomId(anotherUserId: String) = if (authSharedPrefRepository.userRole == 0) {
    "${authSharedPrefRepository.userId}_${anotherUserId}"
  } else {
    "${anotherUserId}_${authSharedPrefRepository.userId}"
  }

  private fun getRoomChatsById(chatRoomId: String) = getChatRoomRef.child(
      chatRoomId).child(ReferenceConstants.CHATS)

  private fun setValue(ref: DatabaseReference, value: Any?): Task<Void> {
    return ref.setValue(value)
  }
}