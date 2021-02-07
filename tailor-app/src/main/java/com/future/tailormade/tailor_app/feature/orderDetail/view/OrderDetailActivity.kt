package com.future.tailormade.tailor_app.feature.orderDetail.view

import android.os.Bundle
import androidx.activity.viewModels
import com.ethanhua.skeleton.SkeletonScreen
import com.future.tailormade.base.view.BaseActivity
import com.future.tailormade.tailor_app.R
import com.future.tailormade.tailor_app.core.model.ui.order.OrderDesignUiModel
import com.future.tailormade.tailor_app.core.model.ui.orderDetail.OrderDetailMeasurementUiModel
import com.future.tailormade.tailor_app.databinding.ActivityOrderDetailBinding
import com.future.tailormade.tailor_app.feature.orderDetail.viewModel.OrderDetailViewModel
import com.future.tailormade.util.extension.hide
import com.future.tailormade.util.extension.show
import com.future.tailormade.util.extension.strikeThrough
import com.future.tailormade.util.image.ImageLoader
import com.future.tailormade_router.actions.Action
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailActivity : BaseActivity() {

  companion object {
    private const val PARAM_ORDER_DETAIL_ID = "ORDER_DETAIL_ID"
  }

  private lateinit var binding: ActivityOrderDetailBinding

  private val viewModel: OrderDetailViewModel by viewModels()

  private var orderDetailSkeletonScreen: SkeletonScreen? = null
  private var paymentInfoSkeletonScreen: SkeletonScreen? = null
  private var designDetailSkeletonScreen: SkeletonScreen? = null
  private var sizeInfoSkeletonScreen: SkeletonScreen? = null

  override fun getScreenName(): String = getOrderDetailId() ?: "Order Detail"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityOrderDetailBinding.inflate(layoutInflater)
    toolbar = binding.topToolbarOrderDetail
    setContentView(binding.root)
    setupToolbar(getScreenName())
    setupObserver()
    showSkeletons()
  }

  private fun getOrderDetailId() = intent?.getStringExtra(PARAM_ORDER_DETAIL_ID)

  private fun hideSkeletons() {
    orderDetailSkeletonScreen?.hide()
    paymentInfoSkeletonScreen?.hide()
    designDetailSkeletonScreen?.hide()
    sizeInfoSkeletonScreen?.hide()
  }

  private fun setupDesignDetailData(design: OrderDesignUiModel) {
    with(binding.layoutDesignDetail) {
      textViewOrderTitle.text = design.title
      textViewOrderColor.text = design.color
      textViewOrderSize.text = design.size

      design.discount?.let { discount ->
        setupDesignDiscount(design.price, discount)
      } ?: run {
        textViewOrderPrice.text = design.price
      }

      ImageLoader.loadImageUrl(this@OrderDetailActivity, design.image, imageViewOrder)

      root.setOnClickListener {
        Action.goToDesignDetail(this@OrderDetailActivity, design.id)
      }
    }
  }

  private fun setupDesignDiscount(price: String, discount: String) {
    with(binding.layoutDesignDetail) {
      textViewOrderPrice.hide()
      textViewOrderPriceBeforeDiscount.show()
      textViewOrderPriceAfterDiscount.show()

      textViewOrderPriceBeforeDiscount.text = price
      textViewOrderPriceBeforeDiscount.strikeThrough()
      textViewOrderPriceAfterDiscount.text = discount
    }
  }

  private fun setupMeasurementDetailData(measurements: OrderDetailMeasurementUiModel) {
    with(binding.layoutSizeInformationDetail) {
      textViewSizeChest.text = measurements.chest
      textViewSizeWaist.text = measurements.waist
      textViewSizeHips.text = measurements.hips
      textViewSizeInseam.text = measurements.inseam
      textViewSizeNeckToWaist.text = measurements.neckToWaist
    }
  }

  private fun setupObserver() {
    getOrderDetailId()?.let { id ->
      viewModel.fetchOrderDetail(id)
    }
    viewModel.orderDetailUiModel.observe(this, {
      it?.let { orderDetail ->
        setupOrderInfoData(orderDetail.id, orderDetail.orderedBy, orderDetail.orderDate)
        setupPaymentData(orderDetail.quantity, orderDetail.totalPrice, orderDetail.totalDiscount,
            orderDetail.paymentTotal)
        setupDesignDetailData(orderDetail.design)
        setupMeasurementDetailData(orderDetail.measurement)
        orderDetail.specialInstructions?.let { instruction ->
          showSpecialInstruction(instruction)
        }
        hideSkeletons()
      }
    })
  }

  private fun setupOrderInfoData(orderId: String, orderedBy: String, orderDate: String) {
    with(binding.layoutCardOrderDetailOrderInfo) {
      textViewHistoryDetailOrderId.text = orderId
      textViewHistoryDetailOrderBy.text = orderedBy
      textViewHistoryDetailOrderDate.text = orderDate
    }
  }

  private fun setupPaymentData(quantity: String, priceTotal: String, discountTotal: String,
      paymentTotal: String) {
    with(binding.layoutPaymentInfo) {
      with(layoutCheckoutTotal) {
        textViewOrderQuantity.text = quantity
        textViewOrderTotalPrice.text = priceTotal
        textViewOrderTotalDiscount.text = discountTotal
      }
      textViewCheckoutPaymentTotal.text = paymentTotal
    }
  }

  private fun showSkeletons() {
    orderDetailSkeletonScreen = getSkeleton(binding.layoutCardOrderDetailOrderInfo.root,
        R.layout.layout_card_order_info_skeleton)
    paymentInfoSkeletonScreen = getSkeleton(binding.layoutPaymentInfo.root,
        R.layout.layout_payment_detail_card_skeleton)
    designDetailSkeletonScreen = getSkeleton(binding.layoutDesignDetail.root,
        R.layout.layout_card_order_skeleton)
    sizeInfoSkeletonScreen = getSkeleton(binding.layoutSizeInformationDetail.root,
        R.layout.layout_size_information_detail_skeleton)
  }

  private fun showSpecialInstruction(instruction: String) {
    with(binding) {
      textViewOrderDetailSpecialInstructions.show()
      textViewOrderDetailSpecialInstructionsTitle.show()
      textViewOrderDetailSpecialInstructions.text = instruction
    }
  }
}