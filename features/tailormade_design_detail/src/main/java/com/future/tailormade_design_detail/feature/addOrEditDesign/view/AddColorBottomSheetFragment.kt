package com.future.tailormade_design_detail.feature.addOrEditDesign.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.future.tailormade.base.view.BaseBottomSheetDialogFragment
import com.future.tailormade_design_detail.databinding.FragmentAddColorBottomSheetBinding

class AddColorBottomSheetFragment : BaseBottomSheetDialogFragment() {

  companion object {
    fun newInstance(onSubmitListener: (String, String) -> Unit, colorName: String? = null,
        color: String? = null) = AddColorBottomSheetFragment().apply {
      this.onSubmitListener = onSubmitListener
      this.colorName = colorName
      this.color = color
    }
  }

  private lateinit var binding: FragmentAddColorBottomSheetBinding
  private lateinit var onSubmitListener: (String, String) -> Unit

  private var colorName: String? = null
  private var color: String? = null

  override fun getScreenName(): String = "Add Color"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentAddColorBottomSheetBinding.inflate(inflater, container, false)
    with(binding) {
      buttonCloseAddColorDialog.setOnClickListener {
        dismissAllowingStateLoss()
      }
      buttonAddNewColor.setOnClickListener {
        onSubmitListener.invoke(editTextColorName.text.toString(), colorPicker.color.toString())
      }
    }
    return binding.root
  }
}