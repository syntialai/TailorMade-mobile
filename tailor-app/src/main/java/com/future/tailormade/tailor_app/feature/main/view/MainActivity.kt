package com.future.tailormade.tailor_app.feature.main.view

import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.tailor_app.R
import com.future.tailormade.tailor_app.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

  private lateinit var binding: ActivityMainBinding

  private var actionMode: ActionMode? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarMain
    setContentView(binding.root)
  }

  private fun getActionModeCallback() = object : ActionMode.Callback {

      override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.menu_contextual_top_nav_main, menu)
        return true
      }

      override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?) = false

      override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?) = when (item?.itemId) {
        R.id.item_delete -> {
          showConfirmDeleteDialog()
          true
        }
        R.id.item_select_all -> {
          setAllSelected()
          true
        }
        else -> false
      }

      override fun onDestroyActionMode(mode: ActionMode?) {
        actionMode = null
      }
    }

  private fun setAllSelected() {
    // TODO: set select all to cards
  }

  private fun showConfirmDeleteDialog() {
    // TODO: show material alert dialog to confirm delete data
  }

  private fun startContextualActionMode() {
    actionMode = startActionMode(getActionModeCallback())
  }
}