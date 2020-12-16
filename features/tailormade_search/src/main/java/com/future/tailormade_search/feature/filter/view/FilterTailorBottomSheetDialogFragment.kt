package com.future.tailormade_search.feature.filter.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.future.tailormade.base.view.BaseBottomSheetDialogFragment
import com.future.tailormade_search.databinding.FragmentFilterTailorBottomSheetDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterTailorBottomSheetDialogFragment : BaseBottomSheetDialogFragment() {

  companion object {
    fun newInstance() = FilterTailorBottomSheetDialogFragment()
  }

  private lateinit var binding: FragmentFilterTailorBottomSheetDialogBinding

  override fun getScreenName(): String = "Filter Tailor Bottom Sheet Dialog Fragment"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentFilterTailorBottomSheetDialogBinding.inflate(inflater,
        container, false)

    with(binding) {
      buttonCloseFilterTailorDialog.setOnClickListener {
        dismiss()
      }

      buttonSubmitTailorFilter.setOnClickListener {
        applyFilter()
      }

      groupSearchLocation.buttonShowAllLocation.setOnClickListener {
        findNavController().navigate(
            FilterTailorBottomSheetDialogFragmentDirections
                .actionFilterTailorBottomSheetDialogFragmentToLocationListFragment())
      }
    }

    return binding.root
  }

  private fun applyFilter() {
    // TODO: Apply filter
    dismiss()
  }
}