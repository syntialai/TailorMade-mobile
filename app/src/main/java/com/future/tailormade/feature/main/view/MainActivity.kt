package com.future.tailormade.feature.main.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.forEach
import com.future.tailormade.R
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.databinding.ActivityMainBinding
import com.future.tailormade.feature.cart.view.CartFragment
import com.future.tailormade.feature.dashboard.view.DashboardFragment
import com.future.tailormade_chat.feature.view.ChatListFragment
import com.future.tailormade_profile.feature.profile.view.ProfileFragment
import com.future.tailormade_router.actions.Action
import com.future.tailormade_router.actions.UserAction
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

  private lateinit var activeFragment: BaseFragment

  private lateinit var binding: ActivityMainBinding

  private val dashboardFragment by lazy { DashboardFragment.newInstance() }
  private val chatFragment by lazy { ChatListFragment.newInstance() }
  private val cartFragment by lazy { CartFragment.newInstance() }
  private val profileFragment by lazy { ProfileFragment.newInstance() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarMain
    setContentView(binding.root)
    setSupportActionBar(toolbar)
    if (savedInstanceState == null) {
      setupBottomNav()
      setupFragments()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_top_nav_main, menu)
    setDashboardOptionMenu()
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.menu_search -> {
        Action.goToSearch(this)
        true
      }
      R.id.menu_order -> {
        UserAction.goToHistory(this)
        true
      }
      R.id.menu_settings -> {
        Action.goToSettings(this)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    setupBottomNav()
    supportFragmentManager.popBackStack()
    setupFragments()
  }

  private fun setupBottomNav() {
    binding.bottomNavMain.setOnNavigationItemSelectedListener {
      when (it.itemId) {
        R.id.menu_home -> {
          goToDashboard()
          true
        }
        R.id.menu_chat -> {
          goToChat()
          true
        }
        R.id.menu_cart -> {
          goToCart()
          true
        }
        R.id.menu_profile -> {
          goToProfile()
          true
        }
        else -> false
      }
    }
  }

  private fun setupFragments() {
    supportFragmentManager.beginTransaction().add(
        binding.frameLayoutContent.id, dashboardFragment).commit()
    supportFragmentManager.popBackStack()
    activeFragment = dashboardFragment
  }

  private fun showFragment(fragment: BaseFragment) {
    supportFragmentManager.beginTransaction().replace(binding.frameLayoutContent.id, fragment).commit()
    supportFragmentManager.popBackStack()
    activeFragment = fragment
  }

  private fun goToCart() {
    resetOptionsMenu()
    showOptionMenu(R.id.menu_order)
    showFragment(cartFragment)
  }

  private fun goToChat() {
    resetOptionsMenu()
    showFragment(chatFragment)
  }

  private fun goToDashboard() {
    setDashboardOptionMenu()
    showFragment(dashboardFragment)
  }

  private fun goToProfile() {
    resetOptionsMenu()
    showOptionMenu(R.id.menu_settings)
    showFragment(profileFragment)
  }

  private fun setDashboardOptionMenu() {
    resetOptionsMenu()
    showOptionMenu(R.id.menu_search)
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