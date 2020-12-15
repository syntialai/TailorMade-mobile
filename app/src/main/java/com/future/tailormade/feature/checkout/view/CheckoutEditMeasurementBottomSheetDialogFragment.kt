package com.future.tailormade.feature.checkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.future.tailormade.base.view.BaseBottomSheetDialogFragment
import com.future.tailormade.databinding.FragmentCheckoutEditMeasurementBottomSheetDialogBinding

class CheckoutEditMeasurementBottomSheetDialogFragment : BaseBottomSheetDialogFragment() {

	companion object {
		fun newInstance() =
				CheckoutEditMeasurementBottomSheetDialogFragment()
	}

	private lateinit var binding: FragmentCheckoutEditMeasurementBottomSheetDialogBinding

	override fun getScreenName(): String = "Edit Measurement"

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?): View {
		binding = FragmentCheckoutEditMeasurementBottomSheetDialogBinding.inflate(inflater, container,
				false)
		return binding.root
	}
}