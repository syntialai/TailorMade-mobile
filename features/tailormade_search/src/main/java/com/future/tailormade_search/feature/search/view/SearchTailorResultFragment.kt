package com.future.tailormade_search.feature.search.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade_search.core.model.response.SearchTailorResponse
import com.future.tailormade_search.databinding.FragmentSearchTailorResultBinding
import com.future.tailormade_search.feature.filter.view.FilterTailorBottomSheetDialogFragment
import com.future.tailormade_search.feature.search.adapter.SearchTailorListAdapter
import com.future.tailormade_search.feature.search.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchTailorResultFragment : BaseFragment() {

  companion object {

    fun newInstance() = SearchTailorResultFragment()
  }

  private lateinit var binding: FragmentSearchTailorResultBinding

  private val viewModel: SearchViewModel by viewModels()

  override fun getLogName(): String = "com.future.tailormade_search.feature.search.view.SearchTailorResultFragment"

  override fun getScreenName(): String = "Search Tailor Result"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = FragmentSearchTailorResultBinding.inflate(inflater, container,
        false)

    with(binding) {
      groupSortAndFilter.chipFilter.setOnClickListener {
        showFilterDialog()
      }

      recyclerViewSearchTailorResult.layoutManager = LinearLayoutManager(
          context)
    }

    return binding.root
  }

  private fun hideNoDataState() {
    with(binding) {
      imageViewNoTailorDataState.remove()
      textViewNoTailorDataState.remove()
      textViewNoTailorDataDescriptionState.remove()
    }
  }

  private fun hideRecyclerView() {
    binding.recyclerViewSearchTailorResult.remove()
  }

  private fun setupAdapter(tailorList: List<SearchTailorResponse>) {
    val adapter = SearchTailorListAdapter(tailorList)
    with(binding.recyclerViewSearchTailorResult) {
      this.adapter = adapter
      adapter.notifyDataSetChanged()
    }
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.listOfTailors.observe(viewLifecycleOwner, {
      setupAdapter(it)
      setupAdapter(it)
      if (it.isEmpty()) {
        showNoDataState()
      } else {
        showRecyclerView()
      }
    })
  }

  private fun showFilterDialog() {
    FilterTailorBottomSheetDialogFragment.newInstance().show(
        parentFragmentManager, getScreenName())
  }

  private fun showNoDataState() {
    with(binding) {
      imageViewNoTailorDataState.show()
      textViewNoTailorDataState.show()
      textViewNoTailorDataDescriptionState.show()
    }
    hideRecyclerView()
  }

  private fun showRecyclerView() {
    binding.recyclerViewSearchTailorResult.show()
    hideNoDataState()
  }
}