package com.future.tailormade_chat.core.repository

import com.future.tailormade_chat.core.model.entity.Chat
import com.future.tailormade_chat.core.model.entity.Session
import com.google.android.gms.tasks.Task
import com.google.firebase.database.Query

interface RealtimeDbRepository {

  fun getChatRooms(): Query

  fun getRoomId(anotherUserId: String): String

  fun getUserChatSessionById(userId: String): Query

  fun addChatRoomAndSession(userId: String, chatRoomId: String, session: Session, chat: Chat): Task<Void>?

  fun updateChatRoom(chatRoomId: String, chat: Chat): Task<Void>

  fun deleteChatRoom(chatRoomId: String): Task<Void>

  fun deleteSessionByUserChatSession(userId: String, userChatId: String): Task<Void>
}