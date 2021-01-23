package com.future.tailormade_search.feature.search.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade_router.actions.UserAction
import com.future.tailormade_search.databinding.FragmentSearchTailorResultBinding
import com.future.tailormade_search.feature.filter.view.FilterTailorBottomSheetDialogFragment
import com.future.tailormade_search.feature.search.adapter.SearchTailorListAdapter
import com.future.tailormade_search.feature.search.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchTailorResultFragment : BaseFragment(), View.OnClickListener {

  companion object {
    fun newInstance() = SearchTailorResultFragment()
  }

  private lateinit var binding: FragmentSearchTailorResultBinding

  private val searchTailorListAdapter by lazy {
    SearchTailorListAdapter(this::goToTailorProfile)
  }
  private val viewModel: SearchViewModel by activityViewModels()

  override fun getLogName() = "com.future.tailormade_search.feature.search.view.SearchTailorResultFragment"

  override fun getScreenName(): String = "Search Tailor Result"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentSearchTailorResultBinding.inflate(inflater, container, false)
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

    viewModel.listOfTailors.observe(viewLifecycleOwner, {
      it?.let { tailors ->
        searchTailorListAdapter.submitList(tailors)
        if (tailors.isEmpty()) {
          showNoDataState()
        } else {
          showRecyclerView()
        }
      }
    })
  }

  private fun goToTailorProfile(id: String) {
    context?.let {
      UserAction.goToTailorProfile(it, id)
    }
  }

  private fun hideNoDataState() {
    binding.groupSearchTailorState.remove()
  }

  private fun hideRecyclerView() {
    binding.recyclerViewSearchTailorResult.remove()
  }

  private fun setupRecyclerView() {
    with(binding.recyclerViewSearchTailorResult) {
      layoutManager = LinearLayoutManager(context)
      adapter = adapter
    }
  }

  private fun showFilterDialog() {
    FilterTailorBottomSheetDialogFragment.newInstance().show(parentFragmentManager, getScreenName())
  }

  private fun showNoDataState() {
    binding.groupSearchTailorState.show()
    hideRecyclerView()
  }

  private fun showRecyclerView() {
    binding.recyclerViewSearchTailorResult.show()
    hideNoDataState()
  }
}