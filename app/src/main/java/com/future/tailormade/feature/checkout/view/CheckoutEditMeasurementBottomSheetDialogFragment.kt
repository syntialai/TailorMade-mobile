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
					this.initAdapter()
					this.insertAdapterValue(values)
				}
	}

	private lateinit var binding: FragmentCheckoutEditMeasurementBottomSheetDialogBinding
	private lateinit var onSubmitListener: (List<String>) -> Unit

	private val editMeasurementAdapter by lazy {
		EditMeasurementFieldAdapter()
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
		setupRecyclerView()
		return binding.root
	}

	private fun initAdapter() {
		val labels = resources.getStringArray(R.array.edit_measurement_type_list)
		val images = arrayListOf<Int>() // TODO: Add static resource
		labels.forEachIndexed { index, label ->
			val editMeasurementUiModel = EditMeasurementFieldUiModel(images[index], label)
			editMeasurementList.add(editMeasurementUiModel)
		}
		editMeasurementAdapter.submitList(editMeasurementList)
	}

	private fun insertAdapterValue(values: MutableList<String>) {
		editMeasurementList.forEachIndexed { index, editMeasurement ->
			editMeasurement.value = values[index]
		}
	}

	private fun setupRecyclerView() {
		with(binding.recyclerViewEditMeasurement) {
			layoutManager = LinearLayoutManager(context)
			adapter = editMeasurementAdapter
		}
	}
}