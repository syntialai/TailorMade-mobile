package com.future.tailormade_search.feature.search.view

import android.os.Bundle
import androidx.activity.viewModels
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.remove
import com.future.tailormade_search.R
import com.future.tailormade_search.databinding.ActivitySearchBinding
import com.future.tailormade_search.feature.search.adapter.SearchPagerAdapter
import com.future.tailormade_search.feature.search.viewModel.SearchViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : BaseActivity() {

  private lateinit var binding: ActivitySearchBinding

  private val viewModel: SearchViewModel by viewModels()

  override fun getScreenName(): String = ""

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySearchBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setupToolbar()

    with(binding.viewSearchField) {
      isIconifiedByDefault = false
      setOnSearchClickListener {
        validateQuery(query.toString())
      }
    }

    setupPagerAdapter()
    setupObserver()
  }

  private fun doSearch(query: String) {
    launchCoroutineOnIO ({
      viewModel.searchDesign(query)
    }, Constants.REFRESH_DELAY_TIME)
    launchCoroutineOnIO ({
      viewModel.searchTailor(query)
    }, Constants.REFRESH_DELAY_TIME)
  }

  private fun hideInitialSearchState() {
    binding.groupSearchState.remove()
  }

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
    setupTabLayout()
  }

  private fun setupTabLayout() {
    with(binding) {
      TabLayoutMediator(tabLayoutSearch, viewPagerSearch) { tab, position ->
        tab.text = when (position) {
          SearchPagerAdapter.DESIGN_FRAGMENT_INDEX -> getString(R.string.design_label)
          SearchPagerAdapter.TAILOR_FRAGMENT_INDEX -> getString(R.string.tailor_label)
          else -> ""
        }
        viewPagerSearch.setCurrentItem(tab.position, true)
      }.attach()
    }
  }

  private fun setupToolbar() {
    toolbar = binding.topToolbarSearch
    setSupportActionBar(toolbar)
    setupOnNavigationIconClicked {
      finish()
    }
    setupToolbar(getScreenName())
  }

  private fun showSearchResultView() {
    if (isSearchResultShown().not()) {
      hideInitialSearchState()
      binding.groupSearchResult.remove()
    }
  }

  private fun validateQuery(query: String) {
    if (viewModel.isQueryValid(query)) {
      doSearch(query)
    }
  }
}