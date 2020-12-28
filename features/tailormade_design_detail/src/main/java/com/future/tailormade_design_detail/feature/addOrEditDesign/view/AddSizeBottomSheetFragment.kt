package com.future.tailormade_design_detail.feature.addOrEditDesign.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.future.tailormade.base.view.BaseBottomSheetDialogFragment
import com.future.tailormade.config.Constants
import com.future.tailormade_design_detail.core.model.ui.SizeDetailUiModel
import com.future.tailormade_design_detail.databinding.FragmentAddSizeBottomSheetBinding

class AddSizeBottomSheetFragment : BaseBottomSheetDialogFragment() {

  companion object {
    fun newInstance(onSubmitListener: (String, SizeDetailUiModel) -> Unit, name: String? = null,
        sizeDetail: SizeDetailUiModel? = null) = AddSizeBottomSheetFragment().apply {
      this.onSubmitListener = onSubmitListener
      this.name = name
      this.sizeDetail = sizeDetail
    }
  }

  private lateinit var binding: FragmentAddSizeBottomSheetBinding
  private lateinit var onSubmitListener: (String, SizeDetailUiModel) -> Unit

  private var name: String? = null
  private var sizeDetail: SizeDetailUiModel? = null

  override fun getScreenName() = "Add Size"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentAddSizeBottomSheetBinding.inflate(inflater, container, false)
    with(binding) {
      buttonCloseAddSizeDialog.setOnClickListener {
        dismissAllowingStateLoss()
      }
      buttonAddNewSize.setOnClickListener {
        validate()
      }

      name?.let {
        editTextSizeName.setText(it)
      }
      sizeDetail?.let {
        setSizeDetailData(it)
      }
    }
    return binding.root
  }

  private fun getSizeDetails(): SizeDetailUiModel {
    with(binding) {
      val chest = editTextSizeChest.text.toString()
      val waist = editTextSizeWaist.text.toString()
      val hips = editTextSizeHips.text.toString()
      val neckToWaist = editTextSizeNeckToWaist.text.toString()
      val inseam = editTextSizeInseam.text.toString()
      return SizeDetailUiModel(chest, waist, hips, neckToWaist, inseam)
    }
  }

  private fun isDataValid(): Boolean {
    with(binding) {
      return editTextSizeName.text.toString().isNotBlank()
             && editTextSizeChest.text.toString().isNotBlank()
             && editTextSizeWaist.text.toString().isNotBlank()
             && editTextSizeHips.text.toString().isNotBlank()
             && editTextSizeNeckToWaist.text.toString().isNotBlank()
             && editTextSizeInseam.text.toString().isNotBlank()
    }
  }

  private fun setErrorMessage() {
    with(binding) {
      if (editTextSizeName.text.toString().isBlank()) {
        editTextSizeName.error = Constants.SIZE_NAME_IS_EMPTY
      }

      if (editTextSizeChest.text.toString().isBlank()) {
        editTextSizeChest.error = Constants.CHEST_SIZE_IS_EMPTY
      }

      if (editTextSizeWaist.text.toString().isBlank()) {
        editTextSizeWaist.error = Constants.WAIST_SIZE_IS_EMPTY
      }

      if (editTextSizeHips.text.toString().isBlank()) {
        editTextSizeHips.error = Constants.HIPS_SIZE_IS_EMPTY
      }

      if (editTextSizeNeckToWaist.text.toString().isBlank()) {
        editTextSizeNeckToWaist.error = Constants.NECK_TO_WAIST_SIZE_IS_EMPTY
      }

      if (editTextSizeInseam.text.toString().isBlank()) {
        editTextSizeInseam.error = Constants.INSEAM_SIZE_IS_EMPTY
      }
    }
  }

  private fun setSizeDetailData(uiModel: SizeDetailUiModel) {
    with(binding) {
      editTextSizeChest.setText(uiModel.chest)
      editTextSizeWaist.setText(uiModel.waist)
      editTextSizeHips.setText(uiModel.hips)
      editTextSizeNeckToWaist.setText(uiModel.neckToWaist)
      editTextSizeInseam.setText(uiModel.inseam)
    }
  }

  private fun validate() {
    if (isDataValid()) {
      onSubmitListener.invoke(binding.editTextSizeName.text.toString(), getSizeDetails())
      dismiss()
    } else {
      setErrorMessage()
    }
  }
}