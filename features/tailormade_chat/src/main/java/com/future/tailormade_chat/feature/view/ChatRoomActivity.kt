package com.future.tailormade_chat.feature.view

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.util.extension.text
import com.future.tailormade_chat.R
import com.future.tailormade_chat.core.model.entity.ChatRoom
import com.future.tailormade_chat.databinding.ActivityChatRoomBinding
import com.future.tailormade_chat.feature.adapter.ChatRoomAdapter
import com.future.tailormade_chat.feature.viewModel.ChatRoomViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatRoomActivity : BaseActivity() {

  companion object {
    private const val PARAM_CHAT_ROOM_ID = "PARAM_CHAT_ROOM_ID"
    private const val PARAM_CHAT_ROOM_USER_NAME = "PARAM_CHAT_ROOM_USER_NAME"
  }

  @Inject
  lateinit var authSharedPrefRepository: AuthSharedPrefRepository

  private lateinit var binding: ActivityChatRoomBinding

  private val viewModel: ChatRoomViewModel by viewModels()

  private val adapterValueEventListener by lazy {
    object : ValueEventListener {

      @RequiresApi(Build.VERSION_CODES.N)
      override fun onDataChange(snapshot: DataSnapshot) {
        val chatRoom = snapshot.getValue(ChatRoom::class.java)
        chatRoom?.chats?.values?.let {
          chatRoomAdapter.submitList(it.toList())
        }
      }

      override fun onCancelled(error: DatabaseError) {
        viewModel.setErrorMessage(getString(R.string.load_chat_data_error_message))
      }
    }
  }
  private val chatRoomAdapter by lazy {
    ChatRoomAdapter(authSharedPrefRepository.userId.orEmpty())
  }

  override fun getScreenName(): String = "Chat"

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityChatRoomBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarChatRoom
    setContentView(binding.root)
    setSupportActionBar(toolbar)
    setupOnNavigationIconClicked {
      finish()
    }
    with(binding.layoutInputTextChatRoom) {
      buttonSendMessage.setOnClickListener {
        sendMessage(editTextMessageChatRoom.text())
      }
    }
    setupRecyclerView()
    getIntentData()
    setupObserver()
  }

  private fun getIntentData() {
    intent.getStringExtra(PARAM_CHAT_ROOM_ID)?.let { chatRoomId ->
      viewModel.setChatRoomId(chatRoomId)
    }
    setupToolbar(intent.getStringExtra(PARAM_CHAT_ROOM_USER_NAME) ?: getScreenName())
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun sendMessage(text: String) {
    if (text.isNotBlank()) {
      viewModel.sendMessage(binding.layoutInputTextChatRoom.editTextMessageChatRoom.text())
    }
  }

  private fun setupObserver() {
    viewModel.chatRoomId.observe(this, {
      viewModel.fetchChatRoomData()
    })

    viewModel.chatRoomContent.observe(this, {
      it.addValueEventListener(adapterValueEventListener)
    })
  }

  private fun setupRecyclerView() {
    with(binding.recyclerViewChatRoom) {
      layoutManager = LinearLayoutManager(this@ChatRoomActivity)
      adapter = chatRoomAdapter
    }
  }
}