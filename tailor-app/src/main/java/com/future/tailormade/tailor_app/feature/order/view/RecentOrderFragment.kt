package com.future.tailormade.tailor_app.feature.order.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.tailor_app.R
import com.future.tailormade.tailor_app.databinding.FragmentRecentOrderBinding
import com.future.tailormade.tailor_app.feature.order.adapter.RecentOrderAdapter
import com.future.tailormade.tailor_app.feature.order.viewModel.RecentOrderViewModel
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade_router.actions.TailorAction
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
      chipFilterAcceptedOrder.setOnClickListener {
        viewModel.fetchAcceptedOrders()
        showEmptyState()
        chipFilterAcceptedOrder.isChecked = true
      }

      chipFilterRejectedOrder.setOnClickListener {
        viewModel.fetchRejectedOrders()
        showEmptyState()
        chipFilterRejectedOrder.isChecked = true
      }
    }
    setupRecyclerView()
    return binding.root
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.fetchAcceptedOrders()
    viewModel.acceptedOrders.observe(viewLifecycleOwner, {
      recentOrderAdapter.submitList(it)
      if (it.isEmpty()) {
        showEmptyState()
      } else {
        showRecyclerView()
      }
    })
    viewModel.rejectedOrders.observe(viewLifecycleOwner, {
      recentOrderAdapter.submitList(it)
      if (it.isEmpty()) {
        showEmptyState()
      } else {
        showRecyclerView()
      }
    })
  }

  private fun goToOrderDetail(id: String) {
    context?.let { context ->
      TailorAction.goToOrderDetail(context, id)
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

      addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
        ContextCompat.getDrawable(context, R.drawable.item_separator)?.let {
          setDrawable(it)
        }
      })
    }
  }

  private fun showEmptyState() {
    binding.layoutRecentOrderState.root.show()
    hideRecyclerView()
  }

  private fun showRecyclerView() {
    binding.recyclerViewRecentOrder.show()
    hideEmptyState()
  }
}