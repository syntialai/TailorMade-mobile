package com.future.tailormade_chat.feature.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.view.BaseSwipeActionCallback
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade_chat.R
import com.future.tailormade_chat.core.model.entity.UserChatSession
import com.future.tailormade_chat.databinding.FragmentChatListBinding
import com.future.tailormade_chat.feature.adapter.ChatListAdapter
import com.future.tailormade_chat.feature.viewModel.ChatListViewModel
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
        val userChatSession = snapshot.value as UserChatSession
        adapter.submitList(userChatSession.sessions.values.toList())
      }

      override fun onCancelled(error: DatabaseError) {
        viewModel.setErrorMessage(getString(R.string.load_chat_data_error_message))
      }
    }
  }
  private val deleteAlertDialog by lazy {
    context?.let {
      MaterialAlertDialogBuilder(it).setTitle(
          resources.getString(R.string.delete_chat_alert_dialog_title)).setNegativeButton(
          R.string.delete_chat_alert_dialog_cancel_button) { dialog, _ ->
        dialog.dismiss()
      }
    }
  }
  private val adapter by lazy { ChatListAdapter() }

  override fun getLogName(): String = "com.future.tailormade_chat.feature.view.ChatListFragment"

  override fun getScreenName(): String = "Chat"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentChatListBinding.inflate(inflater, container, false)

    with(binding) {
      recyclerViewChatList.layoutManager = LinearLayoutManager(context)
      recyclerViewChatList.adapter = adapter
    }

    setupListener()
    setupDeleteSwipeCallback()

    return binding.root
  }

  override fun onDestroyView() {
    viewModel.getChatRooms().removeEventListener(adapterValueEventListener)
    super.onDestroyView()
  }

  private fun setupDeleteSwipeCallback() {
    context?.resources?.let { res ->
      val swipeDeleteCallback = object :
          BaseSwipeActionCallback(res.getColor(R.color.color_red_600),
              res.getDrawable(R.drawable.ic_delete)) {

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder,
            direction: Int) {
          val position = viewHolder.adapterPosition
          val item = adapter.currentList[position]

          showAlertDialogForDeleteChat(item.userId, item.userName, position)
        }
      }

      ItemTouchHelper(swipeDeleteCallback).attachToRecyclerView(
          binding.recyclerViewChatList)
    }
  }

  private fun removeData(userChatId: String, position: Int) {
    viewModel.deleteSessionByUserChatSession(userChatId)?.addOnSuccessListener {
      adapter.notifyItemRemoved(position)
    }?.addOnFailureListener {
      viewModel.setErrorMessage(getString(R.string.delete_chat_error_message))
    }
  }

  private fun setupListener() {
    viewModel.getUserChatSessions()?.addValueEventListener(
        adapterValueEventListener)
  }

  private fun showAlertDialogForDeleteChat(userChatId: String, userName: String,
      position: Int) {
    deleteAlertDialog?.setMessage(resources.getString(
        R.string.delete_chat_alert_dialog_content) + userName)?.setPositiveButton(
        R.string.delete_chat_alert_dialog_delete_button) { dialog, _ ->
      removeData(userChatId, position)
      dialog.dismiss()
    }?.show()
  }
}