package com.future.tailormade_design_detail.feature.addOrEditDesign.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.future.tailormade.base.view.BaseBottomSheetDialogFragment
import com.future.tailormade.util.extension.text
import com.future.tailormade_design_detail.R
import com.future.tailormade_design_detail.core.model.ui.SizeDetailUiModel
import com.future.tailormade_design_detail.databinding.FragmentAddSizeBottomSheetBinding

class AddSizeBottomSheetFragment : BaseBottomSheetDialogFragment() {

  companion object {
    private const val PARAM_SIZE_NAME = "SIZE_NAME"
    private const val PARAM_SIZE_DETAIL = "SIZE_DETAIL"

    fun newInstance(onSubmitListener: (String, SizeDetailUiModel) -> Unit, name: String? = null,
        sizeDetail: SizeDetailUiModel? = null) = AddSizeBottomSheetFragment().apply {
      this.onSubmitListener = onSubmitListener
      this.arguments = Bundle().apply {
        putString(PARAM_SIZE_NAME, name)
        putParcelable(PARAM_SIZE_DETAIL, sizeDetail)
      }
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
      setupData()
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
      val chest = editTextSizeChest.text()
      val waist = editTextSizeWaist.text()
      val hips = editTextSizeHips.text()
      val neckToWaist = editTextSizeNeckToWaist.text()
      val inseam = editTextSizeInseam.text()
      return SizeDetailUiModel(chest, waist, hips, neckToWaist, inseam)
    }
  }

  private fun isDataValid(): Boolean {
    with(binding) {
      return editTextSizeName.text().isNotBlank()
             && editTextSizeChest.text().isNotBlank()
             && editTextSizeWaist.text().isNotBlank()
             && editTextSizeHips.text().isNotBlank()
             && editTextSizeNeckToWaist.text().isNotBlank()
             && editTextSizeInseam.text().isNotBlank()
    }
  }

  private fun setErrorMessage() {
    with(binding) {
      if (editTextSizeName.text().isBlank()) {
        editTextSizeName.error = getString(R.string.size_name_is_empty)
      }

      if (editTextSizeChest.text().isBlank()) {
        editTextSizeChest.error = getString(R.string.chest_size_is_empty)
      }

      if (editTextSizeWaist.text().isBlank()) {
        editTextSizeWaist.error = getString(R.string.waist_size_is_empty)
      }

      if (editTextSizeHips.text().isBlank()) {
        editTextSizeHips.error = getString(R.string.hips_size_is_empty)
      }

      if (editTextSizeNeckToWaist.text().isBlank()) {
        editTextSizeNeckToWaist.error = getString(R.string.neck_to_waist_size_is_empty)
      }

      if (editTextSizeInseam.text().isBlank()) {
        editTextSizeInseam.error = getString(R.string.inseam_size_is_empty)
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

  private fun setupData() {
    arguments?.let {
      name = it.getString(PARAM_SIZE_NAME)
      sizeDetail = it.getParcelable(PARAM_SIZE_DETAIL)
    }
  }

  private fun validate() {
    if (isDataValid()) {
      onSubmitListener.invoke(binding.editTextSizeName.text(), getSizeDetails())
      dismiss()
    } else {
      setErrorMessage()
    }
  }
}