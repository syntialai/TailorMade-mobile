package com.future.tailormade_design_detail.feature.addOrEditDesign.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.future.tailormade.base.view.BaseBottomSheetDialogFragment
import com.future.tailormade.config.Constants
import com.future.tailormade.util.extension.text
import com.future.tailormade_design_detail.databinding.FragmentAddColorBottomSheetBinding
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class AddColorBottomSheetFragment : BaseBottomSheetDialogFragment() {

  companion object {
    private const val PARAM_COLOR = "COLOR"
    private const val PARAM_COLOR_NAME = "COLOR_NAME"

    fun newInstance(onSubmitListener: (String, String) -> Unit, colorName: String? = null,
        color: String? = null) = AddColorBottomSheetFragment().apply {
      this.onSubmitListener = onSubmitListener
      this.arguments = Bundle().apply {
        putString(PARAM_COLOR, color)
        putString(PARAM_COLOR_NAME, colorName)
      }
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
    setupData()
    color?.let {
      setColorPickerColor(it)
    }
    return binding.root
  }

  private fun getColorName() = binding.editTextColorName.text()

  private fun setColorPickerColor(color: String) {
    val colour = Color.parseColor(color)
    binding.viewColorPicker.setInitialColor(colour)
  }

  private fun setupColorPickerView() {
    with(binding.viewColorPicker) {
      attachAlphaSlider(binding.slideBarColorPickerAlpha)
      attachBrightnessSlider(binding.slideBarColorPickerBrightness)
      setColorListener(object : ColorEnvelopeListener {
        override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
          envelope?.color?.let { color ->
            binding.viewColorPreview.setBackgroundColor(color)
          }
          envelope?.hexCode?.let {
            binding.textViewColorPreview.text = "#$it"
          }
        }
      })
    }
  }

  private fun setupData() {
    arguments?.let {
      color = it.getString(PARAM_COLOR)
      colorName = it.getString(PARAM_COLOR_NAME)
    }
  }

  private fun validate() {
    val colorName = getColorName()
    with(binding) {
      if (colorName.isBlank()) {
        textInputColorName.error = Constants.COLOR_NAME_IS_EMPTY
      } else {
        onSubmitListener.invoke(colorName, textViewColorPreview.text())
        dismiss()
      }
    }
  }
}