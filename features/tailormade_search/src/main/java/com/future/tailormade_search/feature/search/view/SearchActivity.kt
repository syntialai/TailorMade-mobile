package com.future.tailormade_search.feature.search.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.util.extension.remove
import com.future.tailormade_search.databinding.ActivitySearchBinding
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

    setupObserver()
  }

  private fun doSearch(query: String) {
    launchCoroutineOnMain { viewModel.searchDesign(query) }
    launchCoroutineOnMain { viewModel.searchTailor(query) }
  }

  private fun isQueryValid(query: String): Boolean = query.isNotBlank() && query.length >= 3

  private fun setupObserver() {
    viewModel.listOfDesigns.observe(this, {
      hideInitialSearchState()
      // TODO: show fragment and pass data to adapter
    })

    viewModel.listOfTailors.observe(this, {
      hideInitialSearchState()
      // TODO: show fragment and pass data to adapter
    })
  }

  private fun validateQuery(query: String) {
    if (isQueryValid(query)) {
      doSearch(query)
    }
  }

  fun hideInitialSearchState() {
    with(binding) {
      imageViewSearchState.remove()
      textViewSearchState.remove()
      textViewSearchDescriptionState.remove()
    }
  }

  fun showSearchResultView() {
    binding.navHostFragment.visibility = View.VISIBLE
  }
}