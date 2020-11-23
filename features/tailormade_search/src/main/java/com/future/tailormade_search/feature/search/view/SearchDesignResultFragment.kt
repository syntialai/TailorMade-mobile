package com.future.tailormade_search.feature.search.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade_search.core.model.response.SearchDesignResponse
import com.future.tailormade_search.databinding.FragmentSearchDesignResultBinding
import com.future.tailormade_search.feature.search.adapter.SearchDesignGridAdapter
import com.future.tailormade_search.feature.search.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDesignResultFragment : BaseFragment() {

  private lateinit var binding: FragmentSearchDesignResultBinding

  private val viewModel: SearchViewModel by viewModels()

  override fun getScreenName(): String = ""

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = FragmentSearchDesignResultBinding.inflate(inflater, container, false)

    with(binding) {
      recyclerViewSearchDesignResult.layoutManager = GridLayoutManager(context, 2)
    }

    return binding.root
  }

  private fun setupAdapter(designList: List<SearchDesignResponse>) {
    val adapter = SearchDesignGridAdapter(designList)
    with(binding.recyclerViewSearchDesignResult) {
      this.adapter = adapter
      adapter.notifyDataSetChanged()
    }
  }

  private fun setupFragmentObserver() {
    viewModel.listOfDesigns.observe(viewLifecycleOwner, {
      setupAdapter(it)
    })
  }

  companion object {

    @JvmStatic
    fun newInstance() = SearchDesignResultFragment()
  }
}