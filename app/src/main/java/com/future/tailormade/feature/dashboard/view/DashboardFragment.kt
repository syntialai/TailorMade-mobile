package com.future.tailormade.feature.dashboard.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.R
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.databinding.FragmentDashboardBinding
import com.future.tailormade.feature.dashboard.adapter.DashboardAdapter
import com.future.tailormade.feature.dashboard.viewModel.DashboardViewModel
import com.future.tailormade.util.extension.orZero
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade_router.actions.Action
import com.future.tailormade_router.actions.UserAction
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class DashboardFragment : BaseFragment() {

  companion object {
    fun newInstance() = DashboardFragment()
  }

  @Inject
  lateinit var authSharedPrefRepository: AuthSharedPrefRepository

  private lateinit var binding: FragmentDashboardBinding

  private val viewModel: DashboardViewModel by viewModels()
  private val dashboardAdapter by lazy {
    DashboardAdapter(::goToTailorProfile, ::goToChatRoom)
  }

  override fun getLogName() = "com.future.tailormade.feature.dashboard.view.DashboardFragment"

  override fun getScreenName(): String = getString(R.string.app_name)

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

  @ExperimentalCoroutinesApi
  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.fetchDashboardTailors()
    viewModel.tailors.observe(viewLifecycleOwner, {
      dashboardAdapter.submitList(it)
      if (it.isNotEmpty()) {
        hideState()
        showRecyclerView()
      } else {
        hideRecyclerView()
        showState()
      }
      binding.swipeRefreshLayoutDashboard.isRefreshing = false
    })
  }

  private fun goToChatRoom(tailorId: String, tailorName: String) {
    context?.let { context ->
      authSharedPrefRepository.userId?.let { userId ->
        Action.goToChatRoom(context, "${userId}_$tailorId", tailorName)
      }
    }
  }

  private fun goToTailorProfile(tailorId: String) {
    context?.let {
      UserAction.goToTailorProfile(it, tailorId)
    }
  }

  private fun hideRecyclerView() {
    binding.recyclerViewTailorList.remove()
  }

  private fun hideState() {
    binding.layoutDashboardState.root.remove()
  }

  private fun showRecyclerView() {
    binding.recyclerViewTailorList.show()
  }

  private fun showState() {
    binding.layoutDashboardState.root.show()
  }

  @ExperimentalCoroutinesApi
  private fun setupRecyclerView() {
    with(binding.recyclerViewTailorList) {
      layoutManager = LinearLayoutManager(context)
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

  @ExperimentalCoroutinesApi
  private fun setupSwipeRefreshLayout() {
    binding.swipeRefreshLayoutDashboard.setOnRefreshListener {
      viewModel.refreshFetch()
      if (binding.swipeRefreshLayoutDashboard.isRefreshing.not()) {
        binding.swipeRefreshLayoutDashboard.isRefreshing = true
      }
    }
  }
}