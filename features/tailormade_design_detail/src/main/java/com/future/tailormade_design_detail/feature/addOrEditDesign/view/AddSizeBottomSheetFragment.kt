package com.future.tailormade_design_detail.feature.addOrEditDesign.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.future.tailormade.base.view.BaseBottomSheetDialogFragment
import com.future.tailormade_design_detail.databinding.FragmentAddSizeBottomSheetBinding

class AddSizeBottomSheetFragment : BaseBottomSheetDialogFragment() {

  companion object {
    // TODO: Add wrapped ui model of size details as param
    fun newInstance(onSubmitListener: (String) -> Unit, name: String? = null) = AddSizeBottomSheetFragment().apply {
      this.onSubmitListener = onSubmitListener
      name?.let { this.name = it }
    }
  }

  private lateinit var binding: FragmentAddSizeBottomSheetBinding
  private lateinit var onSubmitListener: (String) -> Unit

  private var name: String? = null
  // TODO: Define size details here

  override fun getScreenName() = "Add Size"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentAddSizeBottomSheetBinding.inflate(inflater, container, false)
    with(binding) {
      buttonCloseAddSizeDialog.setOnClickListener {
        dismissAllowingStateLoss()
      }
      buttonAddNewSize.setOnClickListener {
        onSubmitListener.invoke(editTextSizeName.text.toString())
        dismiss()
      }

      name?.let {
        editTextSizeName.setText(it)
      }
      // TODO: Set size details here
    }
    return binding.root
  }

  private fun getSizeDetails() {
    with(binding) {
      val chest = editTextSizeChest.text.toString().toDouble()
      val waist = editTextSizeWaist.text.toString().toDouble()
      val hips = editTextSizeHips.text.toString().toDouble()
      val neckToWaist = editTextSizeNeckToWaist.text.toString().toDouble()
      val inseam = editTextSizeInseam.text.toString().toDouble()

      // TODO: wrap in a ui model and return the ui model
    }
  }
}