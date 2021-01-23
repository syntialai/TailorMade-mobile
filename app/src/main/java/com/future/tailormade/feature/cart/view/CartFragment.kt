package com.future.tailormade.feature.cart.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.R
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.databinding.FragmentCartBinding
import com.future.tailormade.feature.cart.adapter.CartAdapter
import com.future.tailormade.feature.cart.viewModel.CartViewModel
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade_router.actions.Action
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class CartFragment : BaseFragment() {

  companion object {
    fun newInstance() = CartFragment()
  }

  private val viewModel: CartViewModel by viewModels()
  private val cartAdapter by lazy {
    CartAdapter(this::deleteItem, this::checkoutItem, this::editItemQuantityListener,
        this::goToDesignDetail)
  }
  private val deleteAlertDialog by lazy {
    context?.let {
      MaterialAlertDialogBuilder(it).setTitle(getString(R.string.cart_delete_alert_dialog_title))
          .setNegativeButton(getString(R.string.cart_delete_alert_dialog_cancel_button)) { dialog, _ ->
            dialog.dismiss()
          }
    }
  }

  private lateinit var binding: FragmentCartBinding

  override fun getLogName(): String = "com.future.tailormade.feature.cart.view.CartFragment"

  override fun getScreenName(): String = "Cart"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentCartBinding.inflate(inflater, container, false)
    setupRecyclerView()
    return binding.root
  }

  @ExperimentalCoroutinesApi
  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.fetchCartData()
    viewModel.cartUiModel.observe(viewLifecycleOwner, {
      cartAdapter.submitList(it)
      if (it.isNotEmpty()) {
        hideState()
        showRecyclerView()
      }
    })
  }

  private fun checkoutItem(id: String) {
    context?.let { context ->
      Action.goToCheckout(context, id)
    }
  }

  private fun deleteItem(id: String, title: String) {
    deleteAlertDialog?.setMessage(getString(R.string.cart_delete_alert_dialog_content, title))?.setPositiveButton(
        getString(R.string.cart_delete_alert_dialog_delete_button)) { dialog, _ ->
      viewModel.deleteCartItem(id)
      dialog.dismiss()
    }?.show()
  }

  private fun editItemQuantityListener(id: String, quantity: Int) {
    viewModel.editCartItemQuantity(id, quantity)
  }

  private fun goToDesignDetail(id: String) {
    context?.let {
      Action.goToDesignDetail(it, id)
    }
  }

  private fun hideState() {
    binding.layoutCartEmptyState.root.remove()
  }

  private fun setupRecyclerView() {
    with(binding.recyclerViewCartList) {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      adapter = cartAdapter
    }
  }

  private fun showRecyclerView() {
    binding.recyclerViewCartList.show()
  }
}