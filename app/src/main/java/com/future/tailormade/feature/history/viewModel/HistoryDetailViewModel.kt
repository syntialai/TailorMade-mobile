package com.future.tailormade.feature.history.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.core.model.response.history.OrderDesignResponse
import com.future.tailormade.core.model.response.history.OrderDetailMeasurementResponse
import com.future.tailormade.core.model.response.history.OrderDetailResponse
import com.future.tailormade.core.model.ui.history.OrderDesignUiModel
import com.future.tailormade.core.model.ui.history.OrderDetailMeasurementUiModel
import com.future.tailormade.core.model.ui.history.OrderDetailUiModel
import com.future.tailormade.core.repository.OrderRepository
import com.future.tailormade.util.extension.onError
import com.future.tailormade.util.extension.toDateString
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class HistoryDetailViewModel @ViewModelInject constructor(
		private val orderRepository: OrderRepository,
		private val authSharedPrefRepository: AuthSharedPrefRepository,
		@Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

	companion object {
		private const val ORDER_DETAIL = "ORDER_DETAIL"
	}

	override fun getLogName() = "com.future.tailormade.feature.history.viewModel.HistoryDetailViewModel"

	private var _orderDetailUiModel: MutableLiveData<OrderDetailUiModel>
	val orderDetailUiModel: LiveData<OrderDetailUiModel>
		get() = _orderDetailUiModel

	init {
		_orderDetailUiModel = savedStateHandle.getLiveData(ORDER_DETAIL, null)
	}

	@ExperimentalCoroutinesApi
	fun fetchHistoryDetails(id: String) {
		launchViewModelScope {
			authSharedPrefRepository.userId?.let { userId ->
				orderRepository.getOrderDetail(userId, id).onStart {
					setStartLoading()
				}.onError {
					setFinishLoading()
					setErrorMessage(Constants.generateFailedFetchError("history with id $id"))
				}.collectLatest { response ->
					response.data?.let { orderDetail ->
						_orderDetailUiModel.value = mapToHistoryDetailUiModel(orderDetail)
						savedStateHandle.set(ORDER_DETAIL, _orderDetailUiModel.value)
					}
				}
			}
		}
	}

	private fun mapToHistoryDetailUiModel(orderDetail: OrderDetailResponse) = OrderDetailUiModel(
			id = orderDetail.id,
			orderDate = orderDetail.createdAt.toDateString(Constants.DD_MMMM_YYYY_HH_MM_SS),
			orderedBy = orderDetail.userName,
			quantity = orderDetail.quantity.toString(),
			status = orderDetail.status,
			specialInstructions = orderDetail.specialInstructions,
			design = mapToHistoryDetailDesign(orderDetail.design),
			measurement = mapToHistoryDetailMeasurementDetail(orderDetail.measurement),
			// TODO: use toIndonesianCurrencyFormat extension
			totalDiscount = orderDetail.totalDiscount.toString(),
			totalPrice = orderDetail.totalPrice.toString(),
			paymentTotal = (orderDetail.totalPrice - orderDetail.totalDiscount).toString(),
	)

	private fun mapToHistoryDetailMeasurementDetail(measurement: OrderDetailMeasurementResponse) = OrderDetailMeasurementUiModel(
			chest = measurement.chest.toString(),
			waist = measurement.waist.toString(),
			hips = measurement.hips.toString(),
			neckToWaist = measurement.neckToWaist.toString(),
			inseam = measurement.inseam.toString()
	)

	private fun mapToHistoryDetailDesign(design: OrderDesignResponse) = OrderDesignUiModel(
			id = design.id,
			image = design.image,
			title = design.title,
			size = design.size,
			color = design.color,
			// TODO: use toIndonesianCurrencyFormat extension
			price = design.price.toString(),
			discount = design.discount.toString(),
	)
}