package com.future.tailormade_chat.core.repository.impl

import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade_chat.core.config.ReferenceConstants
import com.future.tailormade_chat.core.model.entity.Chat
import com.future.tailormade_chat.core.model.entity.Session
import com.future.tailormade_chat.core.model.entity.UserChatSingleSession
import com.future.tailormade_chat.core.repository.RealtimeDbRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import javax.inject.Inject

class RealtimeDbRepositoryImpl @Inject constructor(
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    private val databaseReference: DatabaseReference) : RealtimeDbRepository {

  private val getChatRoomRef = databaseReference.child(
      ReferenceConstants.CHAT_ROOMS)

  private val getUserChatSessionRef = databaseReference.child(
      ReferenceConstants.USERS_CHAT_SESSION)

  override fun getChatRooms(): Query {
    if (authSharedPrefRepository.isUser()) {
      return getChatRoomRef.startAt(authSharedPrefRepository.userId)
    }
    return getChatRoomRef.endAt(authSharedPrefRepository.userId)
  }

  override fun getChatRoomById(chatRoomId: String): Query {
    return getChatRoomRef.child(chatRoomId).orderByChild("chats")
  }

  override fun getRoomId(anotherUserId: String): String = if (authSharedPrefRepository.isUser()) {
    "${authSharedPrefRepository.userId}_${anotherUserId}"
  } else {
    "${anotherUserId}_${authSharedPrefRepository.userId}"
  }

  override fun getUserChatSessionById(userId: String) = getUserChatSessionRef.child(
      userId)

  override fun addChatRoomAndSession(chatRoomId: String, userSession: Session,
      anotherUserSession: Session, chat: Chat): Task<Void> {
    val userChatSession = UserChatSingleSession(userSession.userId.orEmpty(),
        authSharedPrefRepository.name, userSession)
    val anotherUserChatSession = UserChatSingleSession(anotherUserSession.userId.orEmpty(),
        anotherUserSession.userName.orEmpty(), anotherUserSession)

    val childUpdates = hashMapOf<String, Any>(
        "${ReferenceConstants.CHAT_ROOMS}/$chatRoomId/" +
            "${ReferenceConstants.CHATS}/${chat.createdDate.toString()}" to chat).apply {
      putAll(getSessionChild(anotherUserChatSession.userId, userChatSession))
      putAll(getSessionChild(userChatSession.userId, anotherUserChatSession))
    }
    return databaseReference.updateChildren(childUpdates)
  }

  override fun updateReadStatus(userId: String, anotherUserId: String): Task<Void> {
    return setValue(getUserChatSessionById(userId).child(ReferenceConstants.SESSIONS).child(
        anotherUserId).child("hasBeenRead"), true)
  }

  override fun updateChatRoom(chatRoomId: String, chat: Chat): Task<Void> {
    return getRoomChatsById(chatRoomId).updateChildren(
        mutableMapOf<String, Any>(Pair(chat.createdDate.toString(), chat)))
  }

  override fun deleteChatRoom(chatRoomId: String): Task<Void> {
    return setValue(getChatRoomRef.child(chatRoomId), null)
  }

  override fun deleteSessionByUserChatSession(userId: String,
      userChatId: String): Task<Void> {
    return setValue(
        getUserChatSessionById(userId).child(ReferenceConstants.SESSIONS).child(
            userChatId), null)
  }

  private fun getRoomChatsById(chatRoomId: String) = getChatRoomRef.child(
      chatRoomId).child(ReferenceConstants.CHATS)

  private fun getSessionChild(id: String, data: UserChatSingleSession) = hashMapOf(
      "${ReferenceConstants.USERS_CHAT_SESSION}/$id/userId" to data.userId,
      "${ReferenceConstants.USERS_CHAT_SESSION}/$id/userName" to data.userName,
      "${ReferenceConstants.USERS_CHAT_SESSION}/$id/${ReferenceConstants.SESSIONS}/${data.userId}"
          to data.session)

  private fun setValue(ref: DatabaseReference, value: Any?): Task<Void> {
    return ref.setValue(value)
  }
}