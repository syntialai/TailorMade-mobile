package com.future.tailormade_search.feature.search.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade_search.core.model.response.SearchDesignResponse
import com.future.tailormade_search.databinding.FragmentSearchDesignResultBinding
import com.future.tailormade_search.feature.filter.view.FilterDesignBottomSheetDialogFragment
import com.future.tailormade_search.feature.search.adapter.SearchDesignGridAdapter
import com.future.tailormade_search.feature.search.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDesignResultFragment : BaseFragment() {

  companion object {
    fun newInstance() = SearchDesignResultFragment()
  }

  private lateinit var binding: FragmentSearchDesignResultBinding

  private val viewModel: SearchViewModel by viewModels()

  override fun getLogName(): String =
      "com.future.tailormade_search.feature.search.view.SearchDesignResultFragment"

  override fun getScreenName(): String = "Search Design Result"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentSearchDesignResultBinding.inflate(inflater, container,
        false)

    with(binding) {
      groupSortAndFilter.chipFilter.setOnClickListener {
        showFilterDialog()
      }
      recyclerViewSearchDesignResult.layoutManager = GridLayoutManager(context,
          2)
    }

    return binding.root
  }

  private fun hideNoDataState() {
    binding.groupSearchDesignState.remove()
  }

  private fun hideRecyclerView() {
    binding.recyclerViewSearchDesignResult.remove()
  }

  private fun setupAdapter(designList: List<SearchDesignResponse>) {
    val adapter = SearchDesignGridAdapter(designList)
    with(binding.recyclerViewSearchDesignResult) {
      this.adapter = adapter
      adapter.notifyDataSetChanged()
    }
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.listOfDesigns.observe(viewLifecycleOwner, {
      setupAdapter(it)
      if (it.isEmpty()) {
        showNoDataState()
      } else {
        showRecyclerView()
      }
    })
  }

  private fun showFilterDialog() {
    FilterDesignBottomSheetDialogFragment.newInstance().show(
        parentFragmentManager, getScreenName())
  }

  private fun showNoDataState() {
    binding.groupSearchDesignState.show()
    hideRecyclerView()
  }

  private fun showRecyclerView() {
    binding.recyclerViewSearchDesignResult.show()
    hideNoDataState()
  }
}