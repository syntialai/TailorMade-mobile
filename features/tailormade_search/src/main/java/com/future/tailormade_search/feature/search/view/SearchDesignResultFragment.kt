package com.future.tailormade_search.feature.search.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade_router.actions.Action
import com.future.tailormade_search.databinding.FragmentSearchDesignResultBinding
import com.future.tailormade_search.feature.filter.view.FilterDesignBottomSheetDialogFragment
import com.future.tailormade_search.feature.search.adapter.SearchDesignGridAdapter
import com.future.tailormade_search.feature.search.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDesignResultFragment : BaseFragment(), View.OnClickListener {

  companion object {
    fun newInstance() = SearchDesignResultFragment()
  }

  private lateinit var binding: FragmentSearchDesignResultBinding

  private val searchDesignListAdapter by lazy {
    SearchDesignGridAdapter(this::goToDesignDetail)
  }
  private val viewModel: SearchViewModel by activityViewModels()

  override fun getLogName(): String =
      "com.future.tailormade_search.feature.search.view.SearchDesignResultFragment"

  override fun getScreenName(): String = "Search Design Result"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentSearchDesignResultBinding.inflate(inflater, container,
        false)
    setupRecyclerView()
    return binding.root
  }

  override fun onClick(v: View?) {
    with(binding) {
      when(v) {
        groupSortAndFilter.chipFilter -> showFilterDialog()
      }
    }
  }

  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.listOfDesigns.observe(viewLifecycleOwner, {
      it?.let { designs ->
        searchDesignListAdapter.submitList(designs)
        if (designs.isEmpty()) {
          showNoDataState()
        } else {
          showRecyclerView()
        }
      }
    })
  }

  private fun goToDesignDetail(id: String) {
    context?.let {
      Action.goToDesignDetail(it, id)
    }
  }

  private fun hideNoDataState() {
    binding.groupSearchDesignState.remove()
  }

  private fun hideRecyclerView() {
    binding.recyclerViewSearchDesignResult.remove()
  }

  private fun setupRecyclerView() {
    with(binding.recyclerViewSearchDesignResult) {
      layoutManager = GridLayoutManager(context, 2)
      adapter = adapter
    }
  }

  private fun showFilterDialog() {
    FilterDesignBottomSheetDialogFragment.newInstance().show(parentFragmentManager, getScreenName())
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