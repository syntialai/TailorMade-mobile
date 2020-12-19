package com.future.tailormade.feature.checkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.core.model.ui.cart.CartDesignUiModel
import com.future.tailormade.core.model.ui.cart.CartUiModel
import com.future.tailormade.databinding.FragmentThanksForOrderBinding
import com.future.tailormade.feature.checkout.viewModel.ThanksForOrderViewModel
import com.future.tailormade.util.extension.remove
import com.future.tailormade.util.image.ImageLoader
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThanksForOrderFragment : BaseFragment() {

	companion object {
		fun newInstance() = ThanksForOrderFragment()
	}

	private lateinit var binding: FragmentThanksForOrderBinding

	private val args: ThanksForOrderFragmentArgs by navArgs()
	private val viewModel: ThanksForOrderViewModel by viewModels()

	override fun getLogName(): String = "com.future.tailormade.feature.checkout.view.ThanksForOrderFragment"

	override fun getScreenName(): String = "Thank you!"

	override fun getViewModel(): BaseViewModel = viewModel

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?): View {
		binding = FragmentThanksForOrderBinding.inflate(inflater, container, false)

		hideUnusedButton()
		binding.buttonThankYouGoToHistory.setOnClickListener {
			// TODO: Go to history and pass history Id from view model
		}

		return binding.root
	}

	override fun setupFragmentObserver() {
		super.setupFragmentObserver()

		viewModel.setHistoryId(args.historyId)
		viewModel.setCartUiModel(args.cartUiModel)

		viewModel.cartUiModel.observe(viewLifecycleOwner, {
			setupPaymentData(it)
			setupDesignDetailData(it.design)
		})
	}

	private fun hideUnusedButton() {
		with(binding.layoutDesignDetail) {
			spinButtonNumber.remove()
			buttonDeleteOrder.remove()
			buttonCheckoutOrder.remove()
		}
	}

	private fun setupDesignDetailData(design: CartDesignUiModel) {
		with(binding.layoutDesignDetail) {
			textViewOrderTitle.text = design.title
			textViewOrderSize.text = design.size
			textViewOrderColor.text = design.color

			design.discount?.let { discount ->
				textViewOrderBeforeDiscount.text = design.price
				textViewOrderAfterDiscount.text = discount
			} ?: run {
				textViewOrderPrice.text = design.price
			}

			context?.let { context ->
				ImageLoader.loadImageUrl(context, design.image, imageViewOrder)
			}
		}
	}

	private fun setupPaymentData(data: CartUiModel) {
		with(binding) {
			textViewThankYouPaymentTotal.text = data.totalPayment
			textViewThankYouTotalDiscount.text = data.totalDiscount
		}
	}
}