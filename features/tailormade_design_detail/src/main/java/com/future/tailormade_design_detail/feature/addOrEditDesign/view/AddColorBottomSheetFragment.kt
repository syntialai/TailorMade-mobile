package com.future.tailormade_design_detail.feature.addOrEditDesign.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.future.tailormade.base.view.BaseBottomSheetDialogFragment
import com.future.tailormade.config.Constants
import com.future.tailormade_design_detail.databinding.FragmentAddColorBottomSheetBinding
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

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
        validate()
      }
    }
    setupColorPickerView()
    color?.let {
      setColorPickerColor(it)
    }
    return binding.root
  }

  private fun getColorName() = binding.editTextColorName.text.toString()

  private fun setColorPickerColor(color: String) {
    val colour = Color.parseColor(color)
    binding.viewColorPicker.setInitialColor(colour)
  }

  private fun setupColorPickerView() {
    with(binding) {
      viewColorPicker.attachAlphaSlider(slideBarColorPickerAlpha)
      viewColorPicker.attachBrightnessSlider(slideBarColorPickerBrightness)
      viewColorPicker.setColorListener(object: ColorEnvelopeListener {
        override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
          envelope?.color?.let { color ->
            viewColorPreview.setBackgroundColor(color)
          }
          envelope?.hexCode?.let { textViewColorPreview.text = "#$it" }
        }
      })
    }
  }

  private fun validate() {
    val colorName = getColorName()
    with(binding) {
      if (colorName.isBlank()) {
        textInputColorName.error = Constants.COLOR_NAME_IS_EMPTY
      } else {
        onSubmitListener.invoke(colorName, textViewColorPreview.text.toString())
        dismiss()
      }
    }
  }
}