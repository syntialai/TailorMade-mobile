package com.future.tailormade.tailor_app.feature.main.view

import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.forEach
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.tailor_app.R
import com.future.tailormade.tailor_app.databinding.ActivityMainBinding
import com.future.tailormade.tailor_app.feature.dashboard.view.DashboardFragmentDirections
import com.future.tailormade.tailor_app.feature.main.contract.MainDashboardView
import com.future.tailormade_chat.feature.view.ChatListFragmentDirections
import com.future.tailormade_profile.feature.profile.view.ProfileFragmentDirections
import com.future.tailormade_router.actions.Action

class MainActivity : BaseActivity() {

  private var mainDashboardView: MainDashboardView? = null

  private lateinit var binding: ActivityMainBinding

  private var actionMode: ActionMode? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarMain
    setContentView(binding.root)
  }

  override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
    R.id.menu_search -> {
      startActivity(Action.goToSearch(this))
      true
    }
    R.id.menu_settings -> {
      startActivity(Action.goToSettings(this))
      true
    }
    else -> super.onOptionsItemSelected(item)
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
          showDashboardOrOrderOptionsMenu()
          DashboardFragmentDirections.actionGlobalDashboardFragment()
          true
        }
        R.id.menu_chat -> {
          resetOptionsMenu()
          ChatListFragmentDirections.actionGlobalChatListFragment()
          true
        }
        R.id.menu_order -> {
          showDashboardOrOrderOptionsMenu()
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