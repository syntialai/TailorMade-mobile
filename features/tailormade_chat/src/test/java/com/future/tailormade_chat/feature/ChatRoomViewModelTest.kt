package com.future.tailormade_chat.feature

import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.orTrue
import com.future.tailormade_chat.base.BaseViewModelTest
import com.future.tailormade_chat.core.model.entity.Chat
import com.future.tailormade_chat.core.model.entity.Session
import com.future.tailormade_chat.core.model.entity.Text
import com.future.tailormade_chat.core.repository.RealtimeDbRepository
import com.future.tailormade_chat.feature.viewModel.ChatRoomViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class ChatRoomViewModelTest : BaseViewModelTest() {

  companion object {
    private const val CHAT_ROOM_ID = "CHAT ROOM ID"
    private const val ANY_MESSAGE = "ANY MESSAGE"
  }

  private lateinit var viewModel: ChatRoomViewModel

  private val realtimeDbRepository: RealtimeDbRepository = mock()

  private val chatRoomIdCaptor = argumentCaptor<String>()

  private val userIdCaptor = argumentCaptor<String>()

  private val userSessionCaptor = argumentCaptor<Session>()

  private val chatCaptor = argumentCaptor<Chat>()

  @Before
  override fun setUp() {
    viewModel = ChatRoomViewModel(realtimeDbRepository, authSharedPrefRepository, savedStateHandle)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when read chat and userId is exist then success update read status`() {
    val query = anyQuery().stub {
      on { addValueEventListener(any()) } doReturn mock()
    }

    whenever(authSharedPrefRepository.userId) doReturn USER_ID
    whenever(realtimeDbRepository.getUserSession(any(), any())) doReturn query

    viewModel.setChatRoomId(ANOTHER_USER_ID, ANOTHER_USER_NAME)
    viewModel.readChat()

    verify(authSharedPrefRepository).userId
    verify(realtimeDbRepository).getUserSession(userIdCaptor.capture(), userIdCaptor.capture())
    assertEquals(userIdCaptor.firstValue, USER_ID)
    assertEquals(userIdCaptor.secondValue, ANOTHER_USER_ID)

//    verify(realtimeDbRepository).updateReadStatus(userIdCaptor.capture(), userIdCaptor.capture())
//    assertEquals(userIdCaptor.thirdValue, USER_ID)
//    assertEquals(userIdCaptor.lastValue, ANOTHER_USER_ID)

    verifyNoMoreInteractions(authSharedPrefRepository)
  }

  @Test
  fun `Given when read chat and userId is not exist then do nothing`() {
    whenever(authSharedPrefRepository.userId) doReturn null

    viewModel.readChat()

    verify(authSharedPrefRepository).userId
    verifyNoMoreInteractions(authSharedPrefRepository)
    verifyZeroInteractions(realtimeDbRepository)
  }

  @Test
  fun `Given when set chat room id then success update live data`() {
    val expectedResponse = anyQuery()

    whenever(realtimeDbRepository.getRoomId(ANOTHER_USER_ID)) doReturn CHAT_ROOM_ID
    whenever(realtimeDbRepository.getChatRoomById(any())) doReturn expectedResponse

    viewModel.setChatRoomId(ANOTHER_USER_ID, ANOTHER_USER_NAME)

    verify(realtimeDbRepository).getRoomId(ANOTHER_USER_ID)
    verify(realtimeDbRepository).getChatRoomById(chatRoomIdCaptor.capture())
    assertEquals(chatRoomIdCaptor.firstValue, CHAT_ROOM_ID)

    viewModel.chatRoomContent.observeForTesting {
      assertEquals(viewModel.chatRoomContent.value, expectedResponse)
    }

    verifyNoMoreInteractions(realtimeDbRepository)
  }

  @Test
  fun `Given when send message and userId is exist then success add chat room and session and update data`() {
    val expectedTask = getMockedTask(true)

    whenever(authSharedPrefRepository.userId) doReturn USER_ID
    whenever(authSharedPrefRepository.name) doReturn USER_NAME
    whenever(realtimeDbRepository.getRoomId(ANOTHER_USER_ID)) doReturn CHAT_ROOM_ID
    whenever(realtimeDbRepository.getChatRoomById(any())) doReturn anyQuery()
    whenever(realtimeDbRepository.addChatRoomAndSession(any(), any(), any(), any())) doReturn expectedTask

    viewModel.setChatRoomId(ANOTHER_USER_ID, ANOTHER_USER_NAME)
    viewModel.sendMessage(ANY_MESSAGE)

    verify(authSharedPrefRepository).userId
    verify(authSharedPrefRepository).name
    verifyNoMoreInteractions(authSharedPrefRepository)

    verify(realtimeDbRepository).addChatRoomAndSession(chatRoomIdCaptor.capture(),
        userSessionCaptor.capture(), userSessionCaptor.capture(), chatCaptor.capture())
    assertEquals(chatRoomIdCaptor.firstValue, CHAT_ROOM_ID)
    assertUserSession(userSessionCaptor.firstValue,
        Session(userId = ANOTHER_USER_ID, userName = ANOTHER_USER_NAME, hasBeenRead = true))
    assertUserSession(userSessionCaptor.secondValue,
        Session(userId = USER_ID, userName = USER_NAME, hasBeenRead = false))
    assertChat(chatCaptor.firstValue)

//    viewModel.isMessageSent.observeForTesting {
//      assertTrue(viewModel.isMessageSent.value.orFalse())
//    }
  }

  @Test
  fun `Given when send message and userId is exist and realtime db is fail then fail to add chat room and session and update data`() {
    val expectedTask = getMockedTask(false, error = getError())

    whenever(authSharedPrefRepository.userId) doReturn USER_ID
    whenever(authSharedPrefRepository.name) doReturn USER_NAME
    whenever(realtimeDbRepository.getRoomId(ANOTHER_USER_ID)) doReturn CHAT_ROOM_ID
    whenever(realtimeDbRepository.getChatRoomById(any())) doReturn anyQuery()
    whenever(realtimeDbRepository.addChatRoomAndSession(any(), any(), any(), any())) doReturn expectedTask

    viewModel.setChatRoomId(ANOTHER_USER_ID, ANOTHER_USER_NAME)
    viewModel.sendMessage(ANY_MESSAGE)

    verify(authSharedPrefRepository).userId
    verify(authSharedPrefRepository).name
    verifyNoMoreInteractions(authSharedPrefRepository)

    verify(realtimeDbRepository).addChatRoomAndSession(chatRoomIdCaptor.capture(),
        userSessionCaptor.capture(), userSessionCaptor.capture(), chatCaptor.capture())
    assertEquals(chatRoomIdCaptor.firstValue, CHAT_ROOM_ID)
    assertUserSession(userSessionCaptor.firstValue,
        Session(userId = ANOTHER_USER_ID, userName = ANOTHER_USER_NAME, hasBeenRead = true))
    assertUserSession(userSessionCaptor.secondValue,
        Session(userId = USER_ID, userName = USER_NAME, hasBeenRead = false))
    assertChat(chatCaptor.firstValue)

//    viewModel.isMessageSent.observeForTesting {
//      assertFalse(viewModel.isMessageSent.value.orTrue())
//    }
  }

  @Test
  fun `Given when send message and userId is exist but chatRoomId hasn't set then do nothing`() {
    whenever(authSharedPrefRepository.userId) doReturn USER_ID

    viewModel.sendMessage(ANY_MESSAGE)

    verify(authSharedPrefRepository).userId
    verifyNoMoreInteractions(authSharedPrefRepository)
    verifyZeroInteractions(realtimeDbRepository)
  }

  @Test
  fun `Given when send message and userId is not exist then do nothing`() {
    whenever(authSharedPrefRepository.userId) doReturn null

    viewModel.sendMessage(ANY_MESSAGE)

    verify(authSharedPrefRepository).userId
    verifyNoMoreInteractions(authSharedPrefRepository)
    verifyZeroInteractions(realtimeDbRepository)
  }

  @Test
  fun `Given when set is sent then success update live data`() {
    viewModel.setIsSent(false)

    viewModel.isMessageSent.observeForTesting {
      assertFalse(viewModel.isMessageSent.value.orTrue())
    }
  }

  private fun assertUserSession(sessionCaptured: Session, sessionExpected: Session) {
    with(sessionCaptured) {
      assertNotNull(updatedDate)
      assertNotNull(chat)
      assertEquals(userId, sessionExpected.userId)
      assertEquals(userName, sessionExpected.userName)
      assertEquals(hasBeenRead, sessionExpected.hasBeenRead)
    }
  }

  private fun assertChat(chatCaptured: Chat) {
    with(chatCaptured) {
      assertNotNull(createdDate)
      assertEquals(userId, USER_ID)
      assertFalse(hasBeenRead.orTrue())
      assertEquals(type, Constants.MESSAGES_TYPE_TEXT)
      assertEquals(text, Text(ANY_MESSAGE))
    }
  }

  private fun getMockedTask(success: Boolean, error: Exception? = null) = anyTask().stub {
    on { isSuccessful } doReturn success
    on { isComplete } doReturn true
    on { result } doReturn mock()
    on { exception } doReturn error
    on { addOnSuccessListener(any()) } doReturn mock()
    on { addOnFailureListener(any()) } doReturn mock()
  }
}