package com.future.tailormade.tailor_app.feature.main.view

import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.forEach
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.tailor_app.R
import com.future.tailormade.tailor_app.databinding.ActivityMainBinding
import com.future.tailormade.tailor_app.feature.dashboard.view.DashboardFragment
import com.future.tailormade.tailor_app.feature.main.contract.MainDashboardView
import com.future.tailormade.tailor_app.feature.order.view.OrderListFragment
import com.future.tailormade_chat.feature.view.ChatListFragment
import com.future.tailormade_profile.feature.profile.view.ProfileFragment
import com.future.tailormade_router.actions.Action
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

  private lateinit var activeFragment: BaseFragment

  private lateinit var binding: ActivityMainBinding

  private val dashboardFragment by lazy { DashboardFragment.newInstance() }
  private val chatFragment by lazy { ChatListFragment.newInstance() }
  private val orderFragment by lazy { OrderListFragment.newInstance() }
  private val profileFragment by lazy { ProfileFragment.newInstance() }

  private var actionMode: ActionMode? = null

  private var mainDashboardView: MainDashboardView? = null

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
    showDashboardOrOrderOptionsMenu()
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.menu_search -> {
      Action.goToSearch(this)
      true
    }
    R.id.menu_settings -> {
      Action.goToSettings(this)
      true
    }
    else -> super.onOptionsItemSelected(item)
  }

  override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    setupBottomNav()
    setupFragments()
  }

  fun injectMainDashboardView(view: MainDashboardView) {
    mainDashboardView = view
  }

  fun startContextualActionMode() {
    actionMode = startActionMode(getActionModeCallback())
  }

  private fun getActionModeCallback() = object : ActionMode.Callback {

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
      mode?.menuInflater?.inflate(R.menu.menu_contextual_top_nav_main, menu)
      return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = false

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?) = when (item?.itemId) {
      R.id.item_delete -> {
        mainDashboardView?.showConfirmDeleteDialog()
        true
      }
      R.id.item_select_all -> {
        mainDashboardView?.setAllSelected(item.isChecked)
        true
      }
      else -> false
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
      actionMode = null
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
          goToChatList()
          true
        }
        R.id.menu_order -> {
          goToOrderList()
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

  private fun goToDashboard() {
    showDashboardOrOrderOptionsMenu()
    showFragment(dashboardFragment)
  }

  private fun goToChatList() {
    resetOptionsMenu()
    showFragment(chatFragment)
  }

  private fun goToOrderList() {
    resetOptionsMenu()
    showFragment(orderFragment)
  }

  private fun goToProfile() {
    showProfileOptionsMenu()
    showFragment(profileFragment)
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

  private fun showDashboardOrOrderOptionsMenu() {
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