package com.future.tailormade.feature.history.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.future.tailormade.R
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.databinding.FragmentHistoryBinding
import com.future.tailormade.feature.history.adapter.HistoryCardItemAdapter
import com.future.tailormade.feature.history.viewModel.HistoryViewModel
import com.future.tailormade.util.extension.orZero
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class HistoryFragment : BaseFragment() {

  companion object {
    fun newInstance() = HistoryFragment()
  }

  override fun onNavigationIconClicked() {
    activity?.finish()
  }

  private lateinit var binding: FragmentHistoryBinding

  private val viewModel: HistoryViewModel by viewModels()
  private val historyAdapter by lazy {
    HistoryCardItemAdapter(::goToHistoryDetail)
  }

  override fun getLogName() = "com.future.tailormade.feature.history.view.HistoryFragment"

  override fun getScreenName(): String = "Order History"

  override fun getViewModel(): BaseViewModel = viewModel

  @ExperimentalCoroutinesApi
  override fun onCreateView(inflater: LayoutInflater,
      container: ViewGroup?, savedInstanceState: Bundle?): View {
    binding = FragmentHistoryBinding.inflate(inflater, container, false)
    setupRecyclerView()
    setupSwipeRefreshLayout()
    setupSkeleton()
    return binding.root
  }

  @ExperimentalCoroutinesApi
  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.fetchHistory()
    viewModel.orders.observe(viewLifecycleOwner, {
      it?.let { orders ->
        historyAdapter.submitList(orders)
        if (orders.isNotEmpty()) {
          showRecyclerView()
        } else {
          showState()
        }
        with(binding) {
          swipeRefreshLayoutHistory.isRefreshing = false
          recyclerViewHistoryList.post {
            hideSkeleton()
          }
        }
      }
    })
  }

  private fun goToHistoryDetail(id: String) {
    findNavController().navigate(
        HistoryFragmentDirections.actionHistoryFragmentToHistoryDetailFragment(id))
  }

  private fun hideRecyclerView() {
    binding.recyclerViewHistoryList.remove()
  }

  private fun hideState() {
    binding.layoutHistoryState.root.remove()
  }

  @ExperimentalCoroutinesApi
  private fun setupRecyclerView() {
    with(binding.recyclerViewHistoryList) {
      layoutManager = LinearLayoutManager(context)
      adapter = historyAdapter

      addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
        ContextCompat.getDrawable(context, R.drawable.chat_item_separator)?.let {
          setDrawable(it)
        }
      })

      addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
          super.onScrolled(recyclerView, dx, dy)

          if (isLastItemViewed(recyclerView, viewModel.orders.value?.size.orZero())) {
            viewModel.fetchMore()
          }
        }
      })
    }
  }

  private fun setupSkeleton() {
    skeletonScreen = getSkeleton(binding.recyclerViewHistoryList,
        R.layout.layout_history_card_item_skeleton)?.adapter(historyAdapter)?.show()
  }

  @ExperimentalCoroutinesApi
  private fun setupSwipeRefreshLayout() {
    binding.swipeRefreshLayoutHistory.setOnRefreshListener {
      viewModel.refreshFetch()
      if (binding.swipeRefreshLayoutHistory.isRefreshing.not()) {
        binding.swipeRefreshLayoutHistory.isRefreshing = true
      }
    }
  }

  private fun showRecyclerView() {
    binding.recyclerViewHistoryList.show()
    hideState()
  }

  private fun showState() {
    binding.layoutHistoryState.root.show()
    hideRecyclerView()
  }
}