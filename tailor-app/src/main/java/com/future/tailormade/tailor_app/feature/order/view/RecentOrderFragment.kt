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
        chipFilterAcceptedOrder.isChecked = true
      }
      chipFilterRejectedOrder.setOnClickListener {
        viewModel.fetchRejectedOrders()
        chipFilterRejectedOrder.isChecked = true
      }
    }
    setupRecyclerView()
    setupSkeleton()
    return binding.root
  }

  override fun onResume() {
    super.onResume()
    setupSkeleton()
    refreshPage()
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
      binding.recyclerViewRecentOrder.post {
        hideSkeleton()
      }
    })
    viewModel.rejectedOrders.observe(viewLifecycleOwner, {
      recentOrderAdapter.submitList(it)
      if (it.isEmpty()) {
        showEmptyState()
      } else {
        showRecyclerView()
      }
      binding.recyclerViewRecentOrder.post {
        hideSkeleton()
      }
    })
  }

  private fun goToOrderDetail(id: String) {
    context?.let { context ->
      TailorAction.goToOrderDetail(context, id)
    }
  }

  private fun refreshPage() {
    with(binding) {
      if (chipFilterAcceptedOrder.isChecked) {
        viewModel.fetchAcceptedOrders()
      } else {
        viewModel.fetchRejectedOrders()
      }
    }
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

  private fun setupSkeleton() {
    skeletonScreen = getSkeleton(binding.recyclerViewRecentOrder,
        R.layout.layout_card_order_item_recent_skeleton)?.adapter(recentOrderAdapter)?.show()
  }

  private fun showEmptyState() {
    with(binding) {
      layoutRecentOrderState.root.show()
      recyclerViewRecentOrder.remove()
    }
  }

  private fun showRecyclerView() {
    with(binding) {
      recyclerViewRecentOrder.show()
      layoutRecentOrderState.root.remove()
    }
  }
}