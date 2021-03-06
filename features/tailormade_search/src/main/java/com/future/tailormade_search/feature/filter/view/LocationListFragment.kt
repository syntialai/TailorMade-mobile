package com.future.tailormade_search.feature.filter.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade_search.databinding.FragmentLocationListBinding
import com.future.tailormade_search.feature.filter.adapter.ChooseListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationListFragment : BaseFragment() {

  private lateinit var binding: FragmentLocationListBinding

  override fun getLogName(): String = "com.future.tailormade_search.feature.filter.view.LocationListFragment"

  override fun getScreenName(): String = "Select Location"

  override fun getViewModel(): BaseViewModel? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentLocationListBinding.inflate(inflater, container, false)

    with(binding) {
      recyclerViewLocationList.layoutManager = LinearLayoutManager(context)
    }

    showToolbar()

    return binding.root
  }

  // TODO: call onBackPressed for back button in toolbar
  private fun onBackPressed() {
    findNavController().navigateUp()
  }

  private fun onItemClickedListener(position: Int) {
    // TODO: Implement onclick to filter data
  }

  private fun setupAdapter(list: List<Pair<String, Boolean>>) {
    val adapter = ChooseListAdapter(list, this::onItemClickedListener)
    binding.recyclerViewLocationList.adapter = adapter
  }

  companion object {

    @JvmStatic
    fun newInstance() = LocationListFragment()
  }
}