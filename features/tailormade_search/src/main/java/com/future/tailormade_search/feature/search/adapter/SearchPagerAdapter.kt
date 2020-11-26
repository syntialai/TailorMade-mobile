package com.future.tailormade_search.feature.search.adapter

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.future.tailormade_search.feature.search.view.SearchDesignResultFragment
import com.future.tailormade_search.feature.search.view.SearchTailorResultFragment

class SearchPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

  override fun getItemCount(): Int = 2

  override fun createFragment(position: Int) = when (position) {
    0 -> SearchDesignResultFragment.newInstance()
    else -> SearchTailorResultFragment.newInstance()
  }
}