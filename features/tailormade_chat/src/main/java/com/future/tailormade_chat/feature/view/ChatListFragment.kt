package com.future.tailormade_chat.feature.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.view.decoration.BaseSwipeActionCallback
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade_chat.R
import com.future.tailormade_chat.core.model.entity.UserChatSession
import com.future.tailormade_chat.databinding.FragmentChatListBinding
import com.future.tailormade_chat.feature.adapter.ChatListAdapter
import com.future.tailormade_chat.feature.viewModel.ChatListViewModel
import com.future.tailormade_router.actions.Action
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListFragment : BaseFragment() {

  companion object {
    fun newInstance() = ChatListFragment()
  }

  private lateinit var binding: FragmentChatListBinding

  private val viewModel: ChatListViewModel by viewModels()

  private val adapterValueEventListener by lazy {
    object : ValueEventListener {

      @RequiresApi(Build.VERSION_CODES.N)
      override fun onDataChange(snapshot: DataSnapshot) {
        val userChatSession = snapshot.getValue(UserChatSession::class.java)
        userChatSession?.sessions?.let {
          chatListAdapter.submitList(it.toSortedMap().toList())
          showRecyclerView()
        } ?: run {
          showEmptyState()
        }
      }

      override fun onCancelled(error: DatabaseError) {
        viewModel.setErrorMessage(getString(R.string.load_chat_data_error_message))
      }
    }
  }
  private val deleteAlertDialog by lazy {
    context?.let {
      MaterialAlertDialogBuilder(it).setTitle(R.string.delete_chat_alert_dialog_title)
    }
  }
  private val chatListAdapter by lazy {
    ChatListAdapter(this::openChatRoom)
  }

  override fun getLogName(): String = "com.future.tailormade_chat.feature.view.ChatListFragment"

  override fun getScreenName(): String = "Chat"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentChatListBinding.inflate(inflater, container, false)

    setupRecyclerView()
    setupSkeleton()
    setupListener()
    setupDeleteSwipeCallback()

    return binding.root
  }

  override fun onDestroyView() {
    viewModel.getChatRooms().removeEventListener(adapterValueEventListener)
    super.onDestroyView()
  }

  private fun openChatRoom(id: String, name: String) {
    context?.let { context ->
      Action.goToChatRoom(context, id, name)
    }
  }

  private fun removeData(userChatId: String, position: Int) {
    viewModel.deleteSessionByUserChatSession(userChatId)?.addOnSuccessListener {
      chatListAdapter.notifyItemRemoved(position)
    }?.addOnFailureListener {
      viewModel.setErrorMessage(getString(R.string.delete_chat_error_message))
    }
  }

  private fun setupDeleteSwipeCallback() {
    context?.let { context ->
      val swipeDeleteCallback = object :
          BaseSwipeActionCallback(context.getColor(R.color.color_red_600),
              ContextCompat.getDrawable(context, R.drawable.ic_delete)?.apply {
                setTint(context.getColor(R.color.color_white))
              }) {

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
          val position = viewHolder.adapterPosition
          val item = chatListAdapter.currentList[position]

          with(item.second) {
            userId?.let { id ->
              userName?.let { name ->
                showAlertDialogForDeleteChat(id, name, position)
              }
            }
          }
        }
      }

      ItemTouchHelper(swipeDeleteCallback).attachToRecyclerView(binding.recyclerViewChatList)
    }
  }

  private fun setupListener() {
    viewModel.getUserChatSessions()?.addValueEventListener(adapterValueEventListener)
  }

  private fun setupRecyclerView() {
    with(binding.recyclerViewChatList) {
      layoutManager = LinearLayoutManager(context)
      adapter = chatListAdapter

      addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
        ContextCompat.getDrawable(context, R.drawable.chat_list_separator)?.let {
          setDrawable(it)
        }
      })
    }
  }

  private fun setupSkeleton() {
    skeletonScreen = getSkeleton(binding.recyclerViewChatList,
        R.layout.layout_card_chat_skeleton)?.adapter(chatListAdapter)?.count(5)?.show()
  }

  private fun showAlertDialogForDeleteChat(userChatId: String, userName: String, position: Int) {
    deleteAlertDialog?.setMessage(resources.getString(
        R.string.delete_chat_alert_dialog_content) + " $userName")?.setPositiveButton(
        R.string.delete_alert_dialog_delete_button) { dialog, _ ->
      removeData(userChatId, position)
      dialog.dismiss()
    }?.setNegativeButton(R.string.delete_alert_dialog_cancel_button) { dialog, _ ->
      chatListAdapter.notifyItemChanged(position)
      dialog.dismiss()
    }?.show()
  }

  private fun showEmptyState() {
    with(binding) {
      recyclerViewChatList.remove()
      layoutChatListState.root.show()
    }
  }

  private fun showRecyclerView() {
    with(binding) {
      layoutChatListState.root.remove()
      recyclerViewChatList.show()
      recyclerViewChatList.post {
        hideSkeleton()
      }
    }
  }
}