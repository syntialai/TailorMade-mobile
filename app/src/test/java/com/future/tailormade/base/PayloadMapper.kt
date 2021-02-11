package com.future.tailormade.base

import com.future.tailormade.base.test.BaseTest
import com.future.tailormade.core.mock.DataMock
import com.future.tailormade.core.model.response.dashboard.DashboardTailorResponse
import com.future.tailormade.core.model.response.history.OrderDetailMeasurementResponse
import com.future.tailormade.core.model.response.history.OrderDetailResponse
import com.future.tailormade.core.model.response.history.OrderResponse
import com.future.tailormade.core.model.ui.dashboard.DashboardTailorUiModel
import com.future.tailormade.core.repository.OrderRepositoryImplTest
import com.future.tailormade_auth.core.model.response.UserResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
object PayloadMapper {

  fun getDashboardTailorsResponse() = listOf(
      DashboardTailorResponse(id = BaseTest.TAILOR_ID, name = BaseTest.TAILOR_NAME))

  fun getDashboardTailorsUiModel() = arrayListOf(
      DashboardTailorUiModel(id = BaseTest.TAILOR_ID, name = BaseTest.TAILOR_NAME))

//  fun getOrdersResponse() = listOf(
//      OrderResponse(0, getOrderDesignResponse(), OrderRepositoryImplTest.ID, 1, "", "", 0.0, 0.0, 0, OrderRepositoryImplTest.USER_ID))
//
//  fun getOrderDetailResponse() = OrderDetailResponse(0, getOrderDesignResponse(), OrderRepositoryImplTest.ID,
//      getOrderDetailMeasurementResponse(), 1, "", "", "", "", 0.0, 0.0, 0, OrderRepositoryImplTest.USER_ID, "")
//
//  fun getOrderDesignResponse() = DataMock.getOrdersMock()
//
//  fun getOrderDetailMeasurementResponse() = OrderDetailMeasurementResponse(12f, 13f, 14f, 15f,
//      16f)

  fun getUserResponse() = UserResponse(id = BaseTest.USER_ID, name = BaseTest.USER_NAME,
      email = BaseTest.USER_EMAIL, gender = BaseTest.USER_GENDER.name,
      role = BaseTest.USER_ROLE.name)
}