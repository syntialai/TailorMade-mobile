package com.future.tailormade_chat.feature

import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade_chat.base.BaseViewModelTest
import com.future.tailormade_chat.core.repository.RealtimeDbRepository
import com.future.tailormade_chat.feature.viewModel.ChatListViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.database.Query
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class ChatListViewModelTest : BaseViewModelTest() {

  private lateinit var viewModel: ChatListViewModel

  private val realtimeDbRepository: RealtimeDbRepository = mock()

  private val authSharedPrefRepository: AuthSharedPrefRepository = mock()

  @Before
  override fun setUp() {
    viewModel = ChatListViewModel(realtimeDbRepository, authSharedPrefRepository)
  }

  @After
  override fun tearDown() {
    Mockito.framework().clearInlineMocks()
  }

  @Test
  fun `Given when get chat room then success return Query from repository`() {
    whenever(realtimeDbRepository.getChatRooms()) doReturn anyQuery()

    val response = viewModel.getChatRooms()

    verify(realtimeDbRepository).getChatRooms()
    assertNotNull(response)

    verifyNoMoreInteractions(realtimeDbRepository)
  }

  @Test
  fun `Given when get user chat sessions and userId is exist then success return Query from repo`() {
    whenever(authSharedPrefRepository.userId) doReturn USER_ID
    whenever(realtimeDbRepository.getUserChatSessionById(USER_ID)) doReturn anyQuery()

    val response = viewModel.getUserChatSessions()

    verify(authSharedPrefRepository).userId
    verify(realtimeDbRepository).getUserChatSessionById(USER_ID)
    assertNotNull(response)
    assertTrue(response is Query)

    verifyNoMoreInteractions(realtimeDbRepository, authSharedPrefRepository)
  }

  @Test
  fun `Given when get user chat sessions and userId is not exist then return null`() {
    whenever(authSharedPrefRepository.userId) doReturn null

    val response = viewModel.getUserChatSessions()

    verify(authSharedPrefRepository).userId
    verifyZeroInteractions(realtimeDbRepository)
    assertNull(response)

    verifyNoMoreInteractions(authSharedPrefRepository)
  }

  @Test
  fun `Given when delete session by user chat session and userId is exist then success return Task from repo`() {
    whenever(authSharedPrefRepository.userId) doReturn USER_ID
    whenever(realtimeDbRepository
        .deleteSessionByUserChatSession(USER_ID, ANOTHER_USER_ID)) doReturn anyTask()

    val response = viewModel.deleteSessionByUserChatSession(ANOTHER_USER_ID)

    verify(authSharedPrefRepository).userId
    verify(realtimeDbRepository).deleteSessionByUserChatSession(USER_ID, ANOTHER_USER_ID)
    assertNotNull(response)
    assertTrue(response is Task<Void>)

    verifyNoMoreInteractions(realtimeDbRepository, authSharedPrefRepository)
  }

  @Test
  fun `Given when delete session by user chat session and userId is not exist then return null`() {
    whenever(authSharedPrefRepository.userId) doReturn null

    val response = viewModel.deleteSessionByUserChatSession(ANOTHER_USER_ID)

    verify(authSharedPrefRepository).userId
    verifyZeroInteractions(realtimeDbRepository)
    assertNull(response)

    verifyNoMoreInteractions(authSharedPrefRepository)
  }
}