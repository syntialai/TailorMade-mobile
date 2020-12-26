package com.future.tailormade.tailor_app.feature.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.tailor_app.R
import com.future.tailormade.tailor_app.databinding.FragmentDashboardBinding
import com.future.tailormade.tailor_app.feature.dashboard.adapter.DashboardAdapter
import com.future.tailormade.tailor_app.feature.dashboard.viewModel.DashboardViewModel
import com.future.tailormade.tailor_app.feature.main.contract.MainDashboardView
import com.future.tailormade.tailor_app.feature.main.view.MainActivity
import com.future.tailormade.util.extension.orZero
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade_router.actions.Action
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class DashboardFragment : BaseFragment(), MainDashboardView {

  companion object {
    fun newInstance() = DashboardFragment()
  }

  private lateinit var binding: FragmentDashboardBinding

  private val dashboardAdapter by lazy {
    DashboardAdapter(this::goToDesignDetail, this::selectDesign)
  }
  private val deleteDesignDialog by lazy {
    context?.let { context ->
      MaterialAlertDialogBuilder(context).setTitle(R.string.delete_design_alert_dialog_title)
          .setNegativeButton(R.string.delete_alert_dialog_cancel_button) { dialog, _ ->
            dialog.dismiss()
          }
    }
  }
  private val viewModel: DashboardViewModel by viewModels()

  override fun getLogName() =
      "com.future.tailormade.tailor_app.feature.dashboard.view.DashboardFragment"

  override fun getViewModel(): BaseViewModel = viewModel

  @ExperimentalCoroutinesApi
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    (activity as MainActivity).injectMainDashboardView(this)

    binding = FragmentDashboardBinding.inflate(inflater, container, false)
    with(binding) {
      textViewAddDesign.setOnClickListener {
        // TODO: Go to add design page
      }
    }
    setupRecyclerView()
    setupSwipeRefreshLayout()
    return binding.root
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.designs.observe(viewLifecycleOwner, {
      dashboardAdapter.submitList(it)
      if (it.isEmpty()) {
        showState()
        hideRecyclerView()
      } else {
        hideState()
        showRecyclerView()
      }
      binding.swipeRefreshLayoutDashboard.isRefreshing = false
    })
  }

  @ExperimentalCoroutinesApi
  fun setupRecyclerView() {
    with(binding.recyclerViewTailorDesignsList) {
      layoutManager = GridLayoutManager(context, 2)
      adapter = dashboardAdapter

      addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
          super.onScrolled(recyclerView, dx, dy)

          if (isLastItemViewed(recyclerView, viewModel.designs.value?.size.orZero())) {
            viewModel.fetchMore()
          }
        }
      })
    }
  }

  @ExperimentalCoroutinesApi
  fun setupSwipeRefreshLayout() {
    binding.swipeRefreshLayoutDashboard.setOnRefreshListener {
      viewModel.refreshFetch()
      if (binding.swipeRefreshLayoutDashboard.isRefreshing.not()) {
        binding.swipeRefreshLayoutDashboard.isRefreshing = true
      }
    }
  }

  override fun setAllSelected(selected: Boolean) {
    viewModel.selectAllDesigns(selected)
  }

  override fun showConfirmDeleteDialog() {
    deleteDesignDialog?.setMessage(getString(R.string.delete_design_alert_dialog_content,
        viewModel.selectedDesigns.value?.size.orZero()))?.setPositiveButton(
        R.string.delete_alert_dialog_delete_button) { dialog, _ ->
      viewModel.deleteDesign()
      dialog.dismiss()
    }?.show()
  }

  private fun goToDesignDetail(id: String) {
    context?.let { context ->
      Action.goToDesignDetail(context, id)
    }
  }

  private fun hideRecyclerView() {
    binding.recyclerViewTailorDesignsList.remove()
  }

  private fun hideState() {
    binding.layoutDashboardState.root.remove()
  }

  private fun selectDesign(id: String) {
    (activity as MainActivity).startContextualActionMode()
    viewModel.selectDesign(id)
  }

  private fun showRecyclerView() {
    binding.recyclerViewTailorDesignsList.show()
  }

  private fun showState() {
    binding.layoutDashboardState.root.show()
  }
}