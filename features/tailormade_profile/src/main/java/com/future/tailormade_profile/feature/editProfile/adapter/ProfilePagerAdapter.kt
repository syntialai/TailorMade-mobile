package com.future.tailormade_profile.feature.editProfile.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.future.tailormade_profile.feature.profile.view.ProfileAboutFragment
import com.future.tailormade_profile.feature.profile.view.ProfileDesignFragment

class ProfilePagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

  companion object {
    private const val SEARCH_PAGER_COUNT = 2
    const val DESIGN_FRAGMENT_INDEX = 0
    const val ABOUT_FRAGMENT_INDEX = 1
  }

  override fun getItemCount(): Int = SEARCH_PAGER_COUNT

  override fun createFragment(position: Int) = when (position) {
    DESIGN_FRAGMENT_INDEX -> ProfileDesignFragment.newInstance()
    ABOUT_FRAGMENT_INDEX -> ProfileAboutFragment.newInstance()
    else -> Fragment()
  }
}