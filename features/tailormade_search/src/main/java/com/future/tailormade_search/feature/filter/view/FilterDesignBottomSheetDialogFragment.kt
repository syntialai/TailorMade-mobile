package com.future.tailormade_search.feature.filter.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.future.tailormade.base.view.BaseBottomSheetDialogFragment
import com.future.tailormade_search.databinding.FragmentFilterDesignBottomSheetDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterDesignBottomSheetDialogFragment : BaseBottomSheetDialogFragment() {

  private lateinit var binding: FragmentFilterDesignBottomSheetDialogBinding

  override fun getScreenName(): String = "Filter Design Bottom Sheet Dialog Fragment"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = FragmentFilterDesignBottomSheetDialogBinding.inflate(inflater,
        container, false)

    with(binding) {
      buttonCloseFilterDesignDialog.setOnClickListener {
        dismiss()
      }

      buttonSubmitDesignFilter.setOnClickListener {
        dismiss()
      }
    }

    return binding.root
  }

  companion object {

    @JvmStatic fun newInstance() = FilterDesignBottomSheetDialogFragment()
  }
}