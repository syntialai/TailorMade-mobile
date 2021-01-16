package com.future.tailormade.tailor_app.feature.order.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.tailor_app.databinding.FragmentIncomingOrderBinding
import com.future.tailormade.tailor_app.feature.order.adapter.IncomingOrderAdapter
import com.future.tailormade.tailor_app.feature.order.viewModel.IncomingOrderViewModel
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncomingOrderFragment : BaseFragment() {

  companion object {
    fun newInstance() = IncomingOrderFragment()
  }

  private lateinit var binding: FragmentIncomingOrderBinding

  private val incomingOrderAdapter by lazy {
    IncomingOrderAdapter(this::onAcceptOrder, this::onRejectOrder)
  }
  private val viewModel: IncomingOrderViewModel by viewModels()

  override fun getLogName() =
      "com.future.tailormade.tailor_app.feature.order.view.IncomingOrderFragment"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentIncomingOrderBinding.inflate(inflater, container, false)
    setupRecyclerView()
    return binding.root
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.fetchIncomingOrders()
    viewModel.incomingOrders.observe(viewLifecycleOwner, {
      incomingOrderAdapter.submitList(it)
      if (it.isEmpty()) {
        hideRecyclerView()
        showEmptyState()
      } else {
        showRecyclerView()
        hideEmptyState()
      }
    })
    viewModel.hasOrderResponded.observe(viewLifecycleOwner, {
      if (it) {
        viewModel.fetchIncomingOrders()
      }
    })
  }

  private fun hideEmptyState() {
    binding.layoutIncomingOrderState.root.remove()
  }

  private fun hideRecyclerView() {
    binding.recyclerViewIncomingOrder.remove()
  }

  private fun onAcceptOrder(id: String) {
    viewModel.acceptOrder(id)
  }

  private fun onRejectOrder(id: String) {
    viewModel.rejectOrder(id)
  }

  private fun setupRecyclerView() {
    with(binding.recyclerViewIncomingOrder) {
      layoutManager = LinearLayoutManager(context)
      adapter = incomingOrderAdapter
    }
  }

  private fun showEmptyState() {
    binding.layoutIncomingOrderState.root.show()
  }

  private fun showRecyclerView() {
    binding.recyclerViewIncomingOrder.show()
  }
}