package com.future.tailormade.feature.checkout.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ethanhua.skeleton.SkeletonScreen
import com.future.tailormade.R
import com.future.tailormade.base.view.BaseFragment
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.core.model.ui.cart.CartDesignUiModel
import com.future.tailormade.core.model.ui.cart.CartUiModel
import com.future.tailormade.databinding.FragmentCheckoutBinding
import com.future.tailormade.feature.checkout.viewModel.CheckoutViewModel
import com.future.tailormade.util.extension.hide
import com.future.tailormade.util.extension.orEmptyMutableList
import com.future.tailormade.util.extension.show
import com.future.tailormade.util.extension.strikeThrough
import com.future.tailormade.util.extension.text
import com.future.tailormade.util.image.ImageLoader
import com.future.tailormade_router.actions.Action
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class CheckoutFragment : BaseFragment() {

  companion object {
    fun newInstance() = CheckoutFragment()
  }

  private lateinit var binding: FragmentCheckoutBinding

  private val viewModel: CheckoutViewModel by activityViewModels()

  private var paymentInfoSkeletonScreen: SkeletonScreen? = null
  private var designDetailSkeletonScreen: SkeletonScreen? = null

  override fun getLogName() = "com.future.tailormade.feature.checkout.view.CheckoutFragment"

  override fun getScreenName(): String = "Checkout"

  override fun getViewModel(): BaseViewModel = viewModel

  override fun onNavigationIconClicked() {
    activity?.finish()
  }

  @ExperimentalCoroutinesApi
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    binding = FragmentCheckoutBinding.inflate(inflater, container, false)
    with(binding) {
      buttonCheckoutEditMeasurement.setOnClickListener {
        CheckoutEditMeasurementBottomSheetDialogFragment.newInstance(
            viewModel.measurementValues.value.orEmptyMutableList(),
            viewModel::setCheckoutMeasurementDetail).show(parentFragmentManager, getLogName())
      }
      buttonCheckoutMakeOrder.setOnClickListener {
        viewModel.id.value?.let { id ->
          viewModel.checkoutItem(id, editTextSpecialInstruction.text())
        }
      }
    }
    setupSkeleton()
    return binding.root
  }

  @ExperimentalCoroutinesApi
  override fun setupFragmentObserver() {
    super.setupFragmentObserver()

    viewModel.id.observe(viewLifecycleOwner, {
      viewModel.getCartItemById(it)
    })
    viewModel.cartUiModel.observe(viewLifecycleOwner, {
      setupDesignDetailData(it.design)
      setupPaymentData(it)
      paymentInfoSkeletonScreen?.hide()
      designDetailSkeletonScreen?.hide()
    })
    viewModel.historyId.observe(viewLifecycleOwner, { id ->
      viewModel.cartUiModel.value?.let {
        findNavController().navigate(
            CheckoutFragmentDirections.actionCheckoutFragmentToThanksForOrderFragment(id, it))
      }
    })
  }

  private fun goToDesignDetail(id: String) {
    context?.let { context ->
      Action.goToDesignDetail(context, id)
    }
  }

  private fun setupDesignDetailData(design: CartDesignUiModel) {
    with(binding.layoutDesignDetail) {
      textViewOrderTitle.text = design.title
      textViewOrderSize.text = design.size
      textViewOrderColor.text = design.color

      design.discount?.let { discount ->
        showDiscount()
        textViewOrderBeforeDiscount.text = design.price
        textViewOrderBeforeDiscount.strikeThrough()
        textViewOrderAfterDiscount.text = discount
      } ?: run {
        textViewOrderPrice.text = design.price
      }

      context?.let { context ->
        ImageLoader.loadImageUrl(context, design.image, imageViewOrder)
      }

      root.setOnClickListener {
        goToDesignDetail(design.id)
      }
    }
  }

  private fun setupPaymentData(data: CartUiModel) {
    with(binding.layoutCheckoutPaymentDetail) {
      with(layoutCheckoutTotal) {
        textViewOrderQuantity.text = data.quantity.toString()
        textViewOrderTotalDiscount.text = data.totalDiscount
        textViewOrderTotalPrice.text = data.totalPrice
      }
      textViewCheckoutPaymentTotal.text = data.totalPayment
    }
  }

  private fun setupSkeleton() {
    paymentInfoSkeletonScreen = getSkeleton(binding.layoutDesignDetail.root,
        R.layout.layout_cart_item_no_action_skeleton)
    designDetailSkeletonScreen = getSkeleton(binding.layoutCheckoutPaymentDetail.root,
        R.layout.layout_payment_detail_card_skeleton)
  }

  private fun showDiscount() {
    with(binding.layoutDesignDetail) {
      groupDiscountTextView.show()
      textViewOrderPrice.hide()
    }
  }
}