package com.future.tailormade_search.feature.search.view

import android.os.Bundle
import androidx.activity.viewModels
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.extension.show
import com.future.tailormade_search.databinding.ActivitySearchBinding
import com.future.tailormade_search.feature.filter.view.FilterDesignBottomSheetDialogFragment
import com.future.tailormade_search.feature.filter.view.FilterTailorBottomSheetDialogFragment
import com.future.tailormade_search.feature.search.adapter.SearchPagerAdapter
import com.future.tailormade_search.feature.search.viewModel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : BaseActivity() {

  private lateinit var binding: ActivitySearchBinding

  private val viewModel: SearchViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySearchBinding.inflate(layoutInflater)
    setContentView(binding.root)

    with(binding) {
      viewSearchField.setOnSearchClickListener {
        validateQuery(viewSearchField.query.toString())
      }
    }

    setupPagerAdapter()
    setupObserver()
  }

  private fun doSearch(query: String) {
    launchCoroutineOnMain { viewModel.searchDesign(query) }
    launchCoroutineOnMain { viewModel.searchTailor(query) }
  }

  private fun hideInitialSearchState() {
    binding.groupSearchState.remove()
  }

  private fun isQueryValid(query: String): Boolean = query.isNotBlank() && query.length >= 3

  private fun isSearchResultShown() = binding.viewPagerSearch.isShown

  private fun setupObserver() {
    viewModel.searchResultCount.observe(this, {
      if (it > 0) {
        showSearchResultView()
      }
    })
  }

  private fun setupPagerAdapter() {
    binding.viewPagerSearch.adapter = SearchPagerAdapter(supportFragmentManager,
        lifecycle)
  }

  private fun showSearchResultView() {
    if (isSearchResultShown().not()) {
      hideInitialSearchState()
      binding.groupSearchResult.remove()
    }
  }

  private fun validateQuery(query: String) {
    if (isQueryValid(query)) {
      doSearch(query)
    }
  }
}