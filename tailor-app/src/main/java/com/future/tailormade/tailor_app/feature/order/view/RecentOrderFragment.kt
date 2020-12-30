package com.future.tailormade.tailor_app.feature.order.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.tailor_app.databinding.FragmentRecentOrderBinding
import com.future.tailormade.tailor_app.feature.order.adapter.RecentOrderAdapter
import com.future.tailormade.tailor_app.feature.order.viewModel.RecentOrderViewModel
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentOrderFragment : BaseFragment() {

  companion object {
    fun newInstance() = RecentOrderFragment()
  }

  private lateinit var binding: FragmentRecentOrderBinding

  private val recentOrderAdapter by lazy {
    RecentOrderAdapter(this::goToOrderDetail)
  }
  private val viewModel: RecentOrderViewModel by viewModels()

  override fun getLogName() =
      "com.future.tailormade.tailor_app.feature.order.view.RecentOrderFragment"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentRecentOrderBinding.inflate(inflater, container, false)
    with(binding) {
      chipFilterAllOrder.setOnClickListener {
        viewModel.filterAllOrders()
        chipFilterAllOrder.isChecked = true
      }

      chipFilterAcceptedOrder.setOnClickListener {
        viewModel.filterAcceptedOrders()
        chipFilterAcceptedOrder.isChecked = true
      }

      chipFilterRejectedOrder.setOnClickListener {
        viewModel.filterRejectedOrders()
        chipFilterRejectedOrder.isChecked = true
      }
    }
    setupRecyclerView()
    return binding.root
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.fetchIncomingOrders()
    viewModel.recentOrders.observe(viewLifecycleOwner, {
      recentOrderAdapter.submitList(it)
      if (it.isEmpty()) {
        hideRecyclerView()
        showEmptyState()
      } else {
        showRecyclerView()
        hideEmptyState()
      }
    })
  }

  private fun goToOrderDetail(id: String) {
    context?.let { context ->
      // TODO: Call router and go to order detail
    }
  }

  private fun hideEmptyState() {
    binding.layoutRecentOrderState.root.remove()
  }

  private fun hideRecyclerView() {
    binding.recyclerViewRecentOrder.remove()
  }

  private fun setupRecyclerView() {
    with(binding.recyclerViewRecentOrder) {
      layoutManager = LinearLayoutManager(context)
      adapter = recentOrderAdapter
    }
  }

  private fun showEmptyState() {
    binding.layoutRecentOrderState.root.show()
  }

  private fun showRecyclerView() {
    binding.recyclerViewRecentOrder.show()
  }
}