package com.future.tailormade.tailor_app.feature.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
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
import com.future.tailormade_router.actions.TailorAction
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

  override fun getScreenName(): String = getString(R.string.app_name)

  override fun getViewModel(): BaseViewModel = viewModel

  @ExperimentalCoroutinesApi
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    (activity as MainActivity).injectMainDashboardView(this)

    binding = FragmentDashboardBinding.inflate(inflater, container, false)
    binding.buttonAddDesign.setOnClickListener {
      goToDesignDetail()
    }
    setupRecyclerView()
    setupSkeleton()
    setupSwipeRefreshLayout()

    return binding.root
  }

  @ExperimentalCoroutinesApi
  override fun onResume() {
    super.onResume()
    binding.swipeRefreshLayoutDashboard.isRefreshing = true
    viewModel.fetchTailorDesigns()
  }

  @ExperimentalCoroutinesApi
  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.fetchTailorDesigns()
    viewModel.designs.observe(viewLifecycleOwner, {
      it?.let { designs ->
        dashboardAdapter.submitList(designs)
        if (designs.isEmpty()) {
          showState()
        } else {
          showRecyclerView()
        }
        with(binding) {
          swipeRefreshLayoutDashboard.isRefreshing = false
          recyclerViewTailorDesignsList.post {
            hideSkeleton()
          }
        }
      }
    })
  }

  override fun setAllSelected(selected: Boolean) {
    viewModel.selectAllDesigns(selected)
  }

  override fun showConfirmDeleteDialog() {
    deleteDesignDialog?.setMessage(
        getDialogMessage(viewModel.selectedDesigns.value?.size.orZero()))?.setPositiveButton(
        R.string.delete_alert_dialog_delete_button) { dialog, _ ->
      viewModel.deleteDesigns()
      dialog.dismiss()
    }?.show()
  }

  private fun getDialogMessage(quantity: Int) = resources.getQuantityString(
      R.plurals.delete_design_alert_dialog_content, quantity, quantity)

  private fun getDividerItemDecoration(orientation: Int, drawableId: Int) = DividerItemDecoration(context,
      orientation).apply {
    ContextCompat.getDrawable(requireContext(), drawableId)?.let {
      setDrawable(it)
    }
  }

  private fun goToDesignDetail(id: String? = null) {
    context?.let { context ->
      id?.let {
        Action.goToDesignDetail(context, it)
      } ?: run {
        TailorAction.goToAddDesignDetail(context)
      }
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

  @ExperimentalCoroutinesApi
  private fun setupRecyclerView() {
    with(binding.recyclerViewTailorDesignsList) {
      layoutManager = GridLayoutManager(context, 2)
      adapter = dashboardAdapter

      addItemDecoration(
          getDividerItemDecoration(GridLayoutManager.HORIZONTAL, R.drawable.item_separator))
      addItemDecoration(
          getDividerItemDecoration(GridLayoutManager.VERTICAL, R.drawable.chat_item_separator))

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

  private fun setupSkeleton() {
    skeletonScreen = getSkeleton(binding.recyclerViewTailorDesignsList,
        R.layout.layout_card_design_skeleton)?.adapter(dashboardAdapter)?.show()
  }

  @ExperimentalCoroutinesApi
  private fun setupSwipeRefreshLayout() {
    binding.swipeRefreshLayoutDashboard.setOnRefreshListener {
      viewModel.refreshFetch()
      if (binding.swipeRefreshLayoutDashboard.isRefreshing.not()) {
        binding.swipeRefreshLayoutDashboard.isRefreshing = true
      }
    }
  }

  private fun showRecyclerView() {
    binding.recyclerViewTailorDesignsList.show()
    hideState()
  }

  private fun showState() {
    binding.layoutDashboardState.root.show()
    hideRecyclerView()
  }
}