package com.future.tailormade.tailor_app.feature.order.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.tailor_app.R
import com.future.tailormade.tailor_app.databinding.FragmentOrderListBinding
import com.future.tailormade.tailor_app.feature.order.adapter.OrderPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderListFragment : BaseFragment() {

  companion object {
    fun newInstance() = OrderListFragment()
  }

  private lateinit var binding: FragmentOrderListBinding

  override fun getLogName() = "com.future.tailormade.tailor_app.feature.order.view.OrderListFragment"

  override fun getScreenName(): String = "Orders"

  override fun getViewModel(): BaseViewModel? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentOrderListBinding.inflate(inflater, container, false)
    setupViewPager()
    setupTabLayout()
    return binding.root
  }

  private fun setupTabLayout() {
    with(binding) {
      TabLayoutMediator(tabLayoutOrders, viewPagerOrders) { tab, position ->
        tab.text = when (position) {
          OrderPagerAdapter.INCOMING_ORDER_PAGE_INDEX -> getString(R.string.incoming_label)
          OrderPagerAdapter.RECENT_ORDER_PAGE_INDEX -> getString(R.string.recent_label)
          else -> null
        }
      }.attach()
    }
  }

  private fun setupViewPager() {
    val pagerAdapter = OrderPagerAdapter(requireActivity())
    binding.viewPagerOrders.adapter = pagerAdapter
  }
}