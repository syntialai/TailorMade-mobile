package com.future.tailormade_search.feature.search.view

import android.app.SearchManager
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.util.extension.remove
import com.future.tailormade_search.R
import com.future.tailormade_search.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchActivity : BaseActivity() {

  private lateinit var binding: ActivitySearchBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySearchBinding.inflate(layoutInflater)
    setContentView(binding.root)

    with(binding) {
      viewSearchField.setOnQueryTextFocusChangeListener { _, hasFocus ->
        if (hasFocus.not()) {
          // TODO: Debounce and call View Model
        }
      }
    }
  }

  fun hideInitialSearchState() {
    with(binding) {
      imageViewSearchState.remove()
      textViewSearchState.remove()
      textViewSearchDescriptionState.remove()
    }
  }
}