package com.future.tailormade.feature.main.view

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.forEach
import com.future.tailormade.R
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.databinding.ActivityMainBinding
import com.future.tailormade.feature.cart.view.CartFragmentDirections
import com.future.tailormade.feature.dashboard.view.DashboardFragmentDirections
import com.future.tailormade_chat.feature.view.ChatListFragmentDirections
import com.future.tailormade_profile.feature.profile.view.ProfileFragmentDirections
import com.future.tailormade_router.actions.Action
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarMain
    setContentView(binding.root)
    setupBottomNav()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.menu_search -> {
        startActivity(Action.goToSearch(this))
        true
      }
      R.id.menu_order -> {
        startActivity(Action.goToHistory(this))
        true
      }
      R.id.menu_settings -> {
        startActivity(Action.goToSettings(this))
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun setupBottomNav() {
    binding.bottomNavMain.setOnNavigationItemSelectedListener {
      when (it.itemId) {
        R.id.menu_home -> {
          showDashboardOptionsMenu()
          DashboardFragmentDirections.actionGlobalDashboardFragment()
          true
        }
        R.id.menu_chat -> {
          resetOptionsMenu()
          ChatListFragmentDirections.actionGlobalChatListFragment()
          true
        }
        R.id.menu_cart -> {
          showCartOptionsMenu()
          CartFragmentDirections.actionGlobalCartFragment()
          true
        }
        R.id.menu_profile -> {
          showProfileOptionsMenu()
          ProfileFragmentDirections.actionGlobalProfileFragment()
          true
        }
        else -> false
      }
    }
  }

  private fun showCartOptionsMenu() {
    resetOptionsMenu()
    showOptionMenu(R.id.menu_order)
  }

  private fun showDashboardOptionsMenu() {
    resetOptionsMenu()
    showOptionMenu(R.id.menu_search)
  }

  private fun showProfileOptionsMenu() {
    resetOptionsMenu()
    showOptionMenu(R.id.menu_settings)
  }

  private fun resetOptionsMenu() {
    toolbar?.menu?.forEach { item ->
      item.isVisible = false
    }
  }

  private fun showOptionMenu(id: Int) {
    toolbar?.menu?.findItem(id)?.isVisible = true
  }
}