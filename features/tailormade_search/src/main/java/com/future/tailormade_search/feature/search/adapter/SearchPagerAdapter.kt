package com.future.tailormade_search.feature.search.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.future.tailormade_search.feature.search.view.SearchDesignResultFragment
import com.future.tailormade_search.feature.search.view.SearchTailorResultFragment

class SearchPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

  companion object {
    private const val SEARCH_PAGER_COUNT = 2
    const val DESIGN_FRAGMENT_INDEX = 0
    const val TAILOR_FRAGMENT_INDEX = 1
  }

  override fun getItemCount(): Int = SEARCH_PAGER_COUNT

  override fun createFragment(position: Int) = when (position) {
    DESIGN_FRAGMENT_INDEX -> SearchDesignResultFragment.newInstance()
    TAILOR_FRAGMENT_INDEX -> SearchTailorResultFragment.newInstance()
    else -> Fragment()
  }
}