package com.future.tailormade_chat.core.repository

import com.future.tailormade_chat.core.model.entity.Chat
import com.future.tailormade_chat.core.model.entity.Session
import com.google.android.gms.tasks.Task
import com.google.firebase.database.Query

interface RealtimeDbRepository {

  fun getChatRooms(): Query

  fun getChatRoomById(chatRoomId: String): Query

  fun getRoomId(anotherUserId: String): String

  fun getUserChatSessionById(userId: String): Query

  fun getUserSession(userId: String, anotherUserId: String): Query

  fun addChatRoomAndSession(chatRoomId: String, userSession: Session, anotherUserSession: Session,
      chat: Chat): Task<Void>

  fun updateReadStatus(userId: String, anotherUserId: String): Task<Void>

  fun updateChatRoom(chatRoomId: String, chat: Chat): Task<Void>

  fun deleteChatRoom(chatRoomId: String): Task<Void>

  fun deleteSessionByUserChatSession(userId: String, userChatId: String): Task<Void>
}