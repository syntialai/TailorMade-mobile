package com.future.tailormade.tailor_app.feature.order.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OrderFragmentStateAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

  companion object {
    const val ORDER_PAGER_COUNT = 2
  }

  override fun getItemCount() = ORDER_PAGER_COUNT

  override fun createFragment(position: Int): Fragment {
    TODO("Not yet implemented")
  }
}