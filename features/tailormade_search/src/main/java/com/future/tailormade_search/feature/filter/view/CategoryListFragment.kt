package com.future.tailormade_search.feature.filter.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade_search.databinding.FragmentCategoryListBinding
import com.future.tailormade_search.feature.filter.adapter.ChooseListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryListFragment : BaseFragment() {

  private lateinit var binding: FragmentCategoryListBinding

  override fun getScreenName(): String = "Select Category"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = FragmentCategoryListBinding.inflate(inflater, container, false)

    with(binding) {
      recyclerViewCategoryList.layoutManager = LinearLayoutManager(context)
    }

    // TODO: show toolbar

    return binding.root
  }

  private fun onItemClickedListener(position: Int) {
    // TODO: Implement onclick to filter data
  }

  private fun setupAdapter(list: List<Pair<String, Boolean>>) {
    val adapter = ChooseListAdapter(list, this::onItemClickedListener)
    binding.recyclerViewCategoryList.adapter = adapter
  }

  companion object {

    @JvmStatic
    fun newInstance() = CategoryListFragment()
  }
}