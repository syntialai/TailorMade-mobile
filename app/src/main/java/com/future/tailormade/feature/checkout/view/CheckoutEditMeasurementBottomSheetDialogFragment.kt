package com.future.tailormade.feature.checkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.future.tailormade.R
import com.future.tailormade.base.view.BaseBottomSheetDialogFragment
import com.future.tailormade.core.model.ui.checkout.EditMeasurementFieldUiModel
import com.future.tailormade.databinding.FragmentCheckoutEditMeasurementBottomSheetDialogBinding
import com.future.tailormade.feature.checkout.adapter.EditMeasurementFieldAdapter

class CheckoutEditMeasurementBottomSheetDialogFragment : BaseBottomSheetDialogFragment() {

  companion object {
    fun newInstance(values: MutableList<String>, onSubmitListener: (List<String>) -> Unit) =
        CheckoutEditMeasurementBottomSheetDialogFragment().apply {
          this.onSubmitListener = onSubmitListener
          this.adapterValues = values
        }
  }

  private lateinit var binding: FragmentCheckoutEditMeasurementBottomSheetDialogBinding
  private lateinit var onSubmitListener: (List<String>) -> Unit
  private lateinit var adapterValues: MutableList<String>

  private val editMeasurementAdapter by lazy {
    EditMeasurementFieldAdapter(this::onEditTextChanged)
  }
  private val editMeasurementList by lazy {
    mutableListOf<EditMeasurementFieldUiModel>()
  }

  override fun getScreenName(): String = "Edit Measurement"

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentCheckoutEditMeasurementBottomSheetDialogBinding.inflate(inflater, container,
        false)
    with(binding) {
      buttonCloseEditMeasurementDialog.setOnClickListener {
        dismiss()
      }

      buttonSubmitEditMeasurement.setOnClickListener {
        onSubmitListener.invoke(editMeasurementList.map { it.value })
        dismiss()
      }
    }
    initAdapter()
    insertAdapterValue()
    setupRecyclerView()
    return binding.root
  }

  private fun initAdapter() {
    val labels = resources.getStringArray(R.array.edit_measurement_type_list)
    val images = arrayListOf(R.drawable.measurement_chest, R.drawable.measurement_waist,
        R.drawable.measurement_hip, R.drawable.measurement_neck_to_waist,
        R.drawable.measurement_inseam)
    labels.forEachIndexed { index, label ->
      val editMeasurementUiModel = EditMeasurementFieldUiModel(images[index], label)
      editMeasurementList.add(editMeasurementUiModel)
    }
  }

  private fun insertAdapterValue() {
    editMeasurementList.forEachIndexed { index, editMeasurement ->
      editMeasurement.value = adapterValues[index]
    }
    editMeasurementAdapter.submitList(editMeasurementList)
  }

  private fun onEditTextChanged(size: String, position: Int) {
    editMeasurementList[position].value = size
  }

  private fun setupRecyclerView() {
    with(binding.recyclerViewEditMeasurement) {
      layoutManager = LinearLayoutManager(context)
      adapter = editMeasurementAdapter
    }
  }
}