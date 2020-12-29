package com.future.tailormade.tailor_app.feature.order.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.future.tailormade.tailor_app.feature.order.view.IncomingOrderFragment

class OrderFragmentStateAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

  companion object {
    const val ORDER_PAGER_COUNT = 2
    private const val INCOMING_ORDER_PAGE_INDEX = 0
    private const val RECENT_ORDER_PAGE_INDEX = 1
  }

  override fun getItemCount() = ORDER_PAGER_COUNT

  override fun createFragment(position: Int) = when(position) {
    INCOMING_ORDER_PAGE_INDEX -> IncomingOrderFragment.newInstance()
    RECENT_ORDER_PAGE_INDEX -> Fragment()
    else -> Fragment()
  }
}