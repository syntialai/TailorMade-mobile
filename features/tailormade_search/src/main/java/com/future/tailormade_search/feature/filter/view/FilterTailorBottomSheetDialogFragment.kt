package com.future.tailormade_search.feature.filter.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.future.tailormade.base.view.BaseBottomSheetDialogFragment
import com.future.tailormade_search.databinding.FragmentFilterTailorBottomSheetDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterTailorBottomSheetDialogFragment : BaseBottomSheetDialogFragment() {

  private lateinit var binding: FragmentFilterTailorBottomSheetDialogBinding

  override fun getScreenName(): String = "Filter Tailor Bottom Sheet Dialog Fragment"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = FragmentFilterTailorBottomSheetDialogBinding.inflate(inflater,
        container, false)

    with(binding) {
      buttonCloseFilterTailorDialog.setOnClickListener {
        dismiss()
      }

      buttonSubmitTailorFilter.setOnClickListener {
        dismiss()
      }
    }

    return binding.root
  }

  companion object {

    @JvmStatic fun newInstance() = FilterTailorBottomSheetDialogFragment()
  }
}