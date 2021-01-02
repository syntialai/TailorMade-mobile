package com.future.tailormade.feature.main.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
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

  private lateinit var navController: NavController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarMain
    setContentView(binding.root)
    setSupportActionBar(toolbar)
    setupNavController()
    setupBottomNav()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_top_nav_main, menu)
    setDashboardOptionMenu()
    return true
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
          goToDashboard()
          true
        }
        R.id.menu_chat -> {
          resetOptionsMenu()
          navController.navigate(ChatListFragmentDirections.actionGlobalChatListFragment())
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

  private fun setupNavController() {
    val hostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_main_fragment) as NavHostFragment
    navController = hostFragment.navController
  }

  private fun goToCart() {
    resetOptionsMenu()
    showOptionMenu(R.id.menu_order)
    navController.navigate(CartFragmentDirections.actionGlobalCartFragment())
  }

  private fun goToDashboard() {
    setDashboardOptionMenu()
    navController.navigate(DashboardFragmentDirections.actionGlobalDashboardFragment())
  }

  private fun setDashboardOptionMenu() {
    resetOptionsMenu()
    showOptionMenu(R.id.menu_search)
  }

  private fun goToProfile() {
    resetOptionsMenu()
    showOptionMenu(R.id.menu_settings)
    navController.navigate(ProfileFragmentDirections.actionGlobalProfileFragment())
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