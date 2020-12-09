package com.future.tailormade.feature.dashboard.view

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.databinding.FragmentDashboardBinding
import com.future.tailormade.feature.dashboard.adapter.DashboardAdapter
import com.future.tailormade.feature.dashboard.viewModel.DashboardViewModel
import com.future.tailormade.util.extension.orZero
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class DashboardFragment : BaseFragment() {

  companion object {
    private const val REFRESH_DELAY_TIME = 1000L

    fun newInstance() = DashboardFragment()
  }

  private lateinit var binding: FragmentDashboardBinding

  private val viewModel: DashboardViewModel by viewModels()
  private val dashboardAdapter by lazy {
    DashboardAdapter()
  }

  override fun getLogName() = "com.future.tailormade.feature.dashboard.view.DashboardFragment"

  override fun getScreenName(): String = "Home"

  override fun getViewModel(): BaseViewModel = viewModel

  @ExperimentalCoroutinesApi
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentDashboardBinding.inflate(inflater, container, false)
    setupRecyclerView()
    setupSwipeRefreshLayout()
    hideRecyclerView()
    showState()
    return binding.root
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.tailors.observe(viewLifecycleOwner, {
      dashboardAdapter.submitList(it)
      hideState()
      showRecyclerView()
    })
  }

  private fun hideRecyclerView() {
    binding.recyclerViewTailorList.remove()
  }

  private fun hideState() {
    with(binding) {
      imageViewDashboardState.remove()
      textViewDashboardTitleState.remove()
      textViewDashboardDescriptionState.remove()
    }
  }

  private fun showRecyclerView() {
    binding.recyclerViewTailorList.show()
  }

  private fun showState() {
    with(binding) {
      imageViewDashboardState.show()
      textViewDashboardTitleState.show()
      textViewDashboardDescriptionState.show()
    }
  }

  @ExperimentalCoroutinesApi
  private fun setupRecyclerView() {
    with(binding.recyclerViewTailorList) {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      adapter = dashboardAdapter

      addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
          super.onScrolled(recyclerView, dx, dy)

          if (isLastItemViewed(recyclerView, viewModel.tailors.value?.size.orZero())) {
            viewModel.fetchMore()
          }
        }
      })
    }
  }

  private fun setupSwipeRefreshLayout() {
    binding.swipeRefreshLayoutDashboard.setOnRefreshListener {
      // TODO: call view model to fetch data
      Handler().postDelayed({
        if (binding.swipeRefreshLayoutDashboard.isRefreshing) {
          binding.swipeRefreshLayoutDashboard.isRefreshing = false
        }
      }, REFRESH_DELAY_TIME)
    }
  }
}