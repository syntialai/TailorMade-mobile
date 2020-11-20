package com.future.tailormade_search.feature.view

import android.os.Bundle
import android.view.View
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade_search.databinding.ActivitySearchBinding

class SearchActivity : BaseActivity() {

  private lateinit var binding: ActivitySearchBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySearchBinding.inflate(layoutInflater)
    setContentView(binding.root)
  }

  fun hideInitialSearchState() {
    with(binding) {
      imageViewSearchState.visibility = View.GONE
      textViewSearchState.visibility = View.GONE
      textViewSearchDescriptionState.visibility = View.GONE
    }
  }

  fun showFragment() {

  }
}