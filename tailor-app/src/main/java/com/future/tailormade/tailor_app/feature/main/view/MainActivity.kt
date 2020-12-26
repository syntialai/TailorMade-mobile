package com.future.tailormade.tailor_app.feature.main.view

import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.tailor_app.R
import com.future.tailormade.tailor_app.databinding.ActivityMainBinding
import com.future.tailormade.tailor_app.feature.main.contract.MainDashboardView

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
}