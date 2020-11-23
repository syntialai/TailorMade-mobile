package com.future.tailormade_search.feature.search.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade_search.core.model.response.SearchTailorResponse
import com.future.tailormade_search.databinding.FragmentSearchTailorResultBinding
import com.future.tailormade_search.feature.search.adapter.SearchTailorListAdapter
import com.future.tailormade_search.feature.search.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchTailorResultFragment : BaseFragment() {

  private lateinit var binding: FragmentSearchTailorResultBinding

  private val viewModel: SearchViewModel by viewModels()

  override fun getScreenName(): String = ""

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = FragmentSearchTailorResultBinding.inflate(inflater, container,
        false)

    with(binding) {
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

  private fun setupFragmentObserver() {
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

  companion object {

    @JvmStatic fun newInstance() = SearchTailorResultFragment()
  }
}