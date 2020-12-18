package com.future.tailormade.feature.history.view

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
import com.future.tailormade.config.Constants
import com.future.tailormade.databinding.FragmentHistoryBinding
import com.future.tailormade.feature.history.adapter.HistoryCardItemAdapter
import com.future.tailormade.feature.history.viewModel.HistoryViewModel
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class HistoryFragment : BaseFragment() {

	companion object {
		fun newInstance() = HistoryFragment()
	}

	private lateinit var binding: FragmentHistoryBinding

	private val viewModel: HistoryViewModel by viewModels()
	private val historyAdapter by lazy {
		HistoryCardItemAdapter()
	}

	override fun getLogName() = "com.future.tailormade.feature.history.view.HistoryFragment"

	override fun getScreenName(): String = "History"

	override fun getViewModel(): BaseViewModel = viewModel

	@ExperimentalCoroutinesApi
	override fun onCreateView(inflater: LayoutInflater,
			container: ViewGroup?, savedInstanceState: Bundle?): View {
		binding = FragmentHistoryBinding.inflate(inflater, container, false)
		setupRecyclerView()
		setupSwipeRefreshLayout()
		return binding.root
	}

	override fun setupFragmentObserver() {
		super.setupFragmentObserver()

		// TODO: observe here
	}

	private fun hideRecyclerView() {
		binding.recyclerViewHistoryList.remove()
	}

	private fun hideState() {
		binding.layoutHistoryState.root.remove()
	}

	private fun showRecyclerView() {
		binding.recyclerViewHistoryList.show()
	}

	private fun showState() {
		binding.layoutHistoryState.root.show()
	}

	@ExperimentalCoroutinesApi
	private fun setupRecyclerView() {
		with(binding.recyclerViewHistoryList) {
			layoutManager = LinearLayoutManager(context)
			adapter = historyAdapter

			addOnScrollListener(object : RecyclerView.OnScrollListener() {

				override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
					super.onScrolled(recyclerView, dx, dy)

					// TODO: Uncomment after viewmodel set
					//					if (isLastItemViewed(recyclerView, viewModel.tailors.value?.size.orZero())) {
					//						viewModel.fetchMore()
					//					}
				}
			})
		}
	}

	private fun setupSwipeRefreshLayout() {
		binding.swipeRefreshLayoutHistory.setOnRefreshListener {
			// TODO: call view model to fetch data
			Handler().postDelayed({
				if (binding.swipeRefreshLayoutHistory.isRefreshing) {
					binding.swipeRefreshLayoutHistory.isRefreshing = false
				}
			}, Constants.REFRESH_DELAY_TIME)
		}
	}
}