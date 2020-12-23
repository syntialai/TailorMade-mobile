package com.future.tailormade.tailor_app.feature.dashboard.view

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.tailor_app.feature.dashboard.viewModel.DashboardViewModel
import com.future.tailormade.tailor_app.databinding.FragmentDashboardBinding
import com.future.tailormade.tailor_app.feature.dashboard.adapter.DashboardAdapter
import com.future.tailormade.util.extension.orZero
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class DashboardFragment : BaseFragment() {

  companion object {
    fun newInstance() = DashboardFragment()
  }

  private lateinit var binding: FragmentDashboardBinding

  private val dashboardAdapter by lazy {
    DashboardAdapter(this::goToDesignDetail)
  }
  private val viewModel: DashboardViewModel by viewModels()

  override fun getLogName() =
      "com.future.tailormade.feature.dashboard.view.DashboardFragment.${Constants.TAILOR}"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentDashboardBinding.inflate(inflater, container, false)
    with(binding) {
      textViewAddDesign.setOnClickListener {
        // TODO: Go to add design page
      }
    }
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

  private fun goToDesignDetail(id: String) {
    // TODO: Route to design detail
  }

  private fun hideRecyclerView() {
    binding.recyclerViewTailorDesignsList.remove()
  }

  private fun hideState() {
    binding.layoutDashboardState.root.remove()
  }

  private fun showRecyclerView() {
    binding.recyclerViewTailorDesignsList.show()
  }

  private fun showState() {
    binding.layoutDashboardState.root.show()
  }
}