package com.future.tailormade.tailor_app.feature.order.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.tailor_app.databinding.FragmentOrderListBinding
import com.future.tailormade.tailor_app.feature.order.adapter.OrderFragmentStateAdapter
import com.future.tailormade.tailor_app.feature.order.viewModel.OrderListViewModel
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderListFragment : BaseFragment() {

  companion object {
    fun newInstance() = OrderListFragment()
  }

  private lateinit var binding: FragmentOrderListBinding

  private val viewModel: OrderListViewModel by viewModels()

  override fun getLogName() = "com.future.tailormade.tailor_app.feature.order.view.OrderListFragment"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentOrderListBinding.inflate(inflater, container, false)
    setupViewPager()
    setupTabLayout()
    return binding.root
  }

  private fun hideEmptyState() {
    binding.layoutOrderListState.root.remove()
  }

  private fun hideOrders() {
    binding.groupOrders.remove()
  }

  private fun setupTabLayout() {
    with(binding) {
      TabLayoutMediator(tabLayoutOrders, viewPagerOrders) { _, position ->
        viewPagerOrders.currentItem = position
      }.attach()
    }
  }

  private fun setupViewPager() {
    val pagerAdapter = OrderFragmentStateAdapter(requireActivity())
    binding.viewPagerOrders.adapter = pagerAdapter
  }

  private fun showEmptyState() {
    binding.layoutOrderListState.root.show()
  }

  private fun showOrders() {
    binding.groupOrders.remove()
  }
}